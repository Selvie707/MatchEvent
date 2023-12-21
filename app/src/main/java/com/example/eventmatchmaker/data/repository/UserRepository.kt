package com.example.eventmatchmaker.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.eventmatchmaker.data.StoryPagingSource
import com.example.eventmatchmaker.data.model.UserModel
import com.example.eventmatchmaker.data.pref.Result
import com.example.eventmatchmaker.data.pref.UserPreference
import com.example.eventmatchmaker.data.response.FileUploadResponse
import com.example.eventmatchmaker.data.response.DataItem
import com.example.eventmatchmaker.data.response.LoginResponse
import com.example.eventmatchmaker.data.retrofit.ApiService
import com.example.eventmatchmaker.data.retrofit.ApiServiceFactory
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    private val token: Flow<String> = userPreference.getSession().map { it.token }
    private val tokenLiveData: LiveData<String> = token.asLiveData()

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getUserStories(name: String, category: String, ageLimit: String,
                       priceStart: String, priceEnd: String, startTime: String, startTimeCap: String
    ): LiveData<PagingData<DataItem>> {
        return tokenLiveData.switchMap {
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    StoryPagingSource(it, name, category, ageLimit, priceStart, priceEnd, startTime, startTimeCap)
                }
            ).liveData
        }
    }

    suspend fun getStoriesLocation(): Result<List<DataItem>> {
        return try {
            val apiService = ApiServiceFactory.getApiService(getSession().first().token)
            val response = apiService.getEventsLocation()

            if (response.data != null) {
                Result.Success(response.data)
            } else {
                Result.Failed("onFailure")
            }
        } catch (e: Exception) {
            Result.Failed("onFailure: ${e.message}")
        }
    }

    fun upload(image: File) = liveData {
        emit(Result.Loading)
//        val ageLimitRequestBody = ageLimit?.toString()?.toRequestBody("text/plain".toMediaType())
//        val capacityRequestBody = capacity.toString().toRequestBody("text/plain".toMediaType())
//        val categoriesRequestBody = categories?.toRequestBody("text/plain".toMediaType())
//        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
//        val dressCodeRequestBody = dressCode?.toRequestBody("text/plain".toMediaType())
//        val endTimeRequestBody = endTime.toRequestBody("text/plain".toMediaType())
//        val latRequestBody = lat?.toString()?.toRequestBody()
//        val lonRequestBody = long?.toString()?.toRequestBody()
//        val nameRequestBody = name.toRequestBody("text/plain".toMediaType())
//        val organizerRequestBody = organizer?.toRequestBody("text/plain".toMediaType())
//        val priceRequestBody = price?.toString()?.toRequestBody("text/plain".toMediaType())
//        val startTimeRequestBody = startTime.toRequestBody("text/plain".toMediaType())

        val ageLimitRequestBody = 16?.toString()?.toRequestBody()
        val capacityRequestBody = 62.toString().toRequestBody()
        val categoriesRequestBody = "adventure".toRequestBody("text/plain".toMediaType())
        val descriptionRequestBody = "Event Description".toRequestBody("text/plain".toMediaType())
        val dressCodeRequestBody = "Event Dress Code".toRequestBody("text/plain".toMediaType())
        val endTimeRequestBody = "2023-12-08T20:00:00.000Z".toRequestBody("text/plain".toMediaType())
        val latRequestBody = 3.7628571.toString().toRequestBody()
        val lonRequestBody = 98.6695859.toString().toRequestBody()
        val nameRequestBody = "Event Name".toRequestBody("text/plain".toMediaType())
        val organizerRequestBody = "Event Organizer".toRequestBody("text/plain".toMediaType())
        val priceRequestBody = 120000.toString().toRequestBody()
        val startTimeRequestBody = "2023-12-01T20:00:00.000Z".toRequestBody("text/plain".toMediaType())

        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            requestImageFile
        )
        try {
            val apiService = ApiServiceFactory.getApiService(getSession().first().token)
            val successResponse = apiService.addEvent(multipartBody, ageLimitRequestBody,
                capacityRequestBody, categoriesRequestBody, descriptionRequestBody,
                dressCodeRequestBody, endTimeRequestBody,
                nameRequestBody, organizerRequestBody, priceRequestBody, startTimeRequestBody, latRequestBody, lonRequestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(Result.Failed(errorResponse.message))
            Log.d("Errornya apaa", errorResponse.message)
        } catch (e: Exception) {
            emit(Result.Failed(e.message.toString()))
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
