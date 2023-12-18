package com.example.eventmatchmaker.data.retrofit

import com.example.eventmatchmaker.data.response.FileUploadResponse
import com.example.eventmatchmaker.data.response.GetUserResponse
import com.example.eventmatchmaker.data.response.LoginResponse
import com.example.eventmatchmaker.data.response.RegisterResponse
import com.example.eventmatchmaker.data.response.EventResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("users")
    suspend fun getUsers(): GetUserResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") name: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("events")
    suspend fun getEvents(): EventResponse

    @GET("events")
    suspend fun getEventsLocation(): EventResponse

    @GET("events")
    suspend fun getEventSearch(@QueryMap queryMap: Map<String, String>): EventResponse

    @Multipart
    @POST("event")
    suspend fun addEvent(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): FileUploadResponse
}