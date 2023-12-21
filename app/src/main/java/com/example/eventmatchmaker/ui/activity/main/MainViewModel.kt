package com.example.eventmatchmaker.ui.activity.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.eventmatchmaker.data.model.UserModel
import com.example.eventmatchmaker.data.repository.UserRepository
import com.example.eventmatchmaker.data.response.DataItem
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _storyList = MutableLiveData<List<DataItem>>()
    val storyList: LiveData<List<DataItem>> = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userLocation = MutableLiveData<String>()
    val userLocation: LiveData<String> = _userLocation

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun saveSession(userId: String, name: String, email: String,
                    token: String, location: String) {
        viewModelScope.launch {
            try {
                val userModel = UserModel(
                    userId = userId,
                    name = name,
                    email = email,
                    token = token,
                    location = location,
                    isLogin = true
                )
                repository.saveSession(userModel)
            } catch (e: Exception) {
                Log.e(TAG, "Update data failed: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

//    fun getStoriesLocation() {
//        _isLoading.value = true
//        viewModelScope.launch {
//            when (val result = repository.getStoriesLocation()) {
//                is Result.Success -> {
//                    _storyList.value = result.data!!
//                    _isLoading.value = false
//                }
//                is Result.Failed -> {
//                    Log.e(TAG, "onFailure")
//                    _isLoading.value = false
//                }
//                else -> {
//                    _isLoading.value = false
//                }
//            }
//        }
//    }

    val story: LiveData<PagingData<DataItem>> =
        repository.getUserStories("a", "", "", "", "", "", "").cachedIn(viewModelScope)

    val recommendEvents: LiveData<PagingData<DataItem>> =
        repository.getRecommendEvents().cachedIn(viewModelScope)

    fun updateLocation(context: Context, fusedLocationProviderClient: FusedLocationProviderClient) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If location permissions are not granted, update LiveData with an empty string or handle accordingly
            _userLocation.value = ""
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                val lat = location?.latitude ?: 0.0
                val lon = location?.longitude ?: 0.0

                val address = getAddressFromLocation(context, lat, lon)

                _userLocation.value = address
            }
        }
    }

    private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                val stringBuilder = StringBuilder()

                for (i in 0..address.maxAddressLineIndex) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n")
                }

                addressText = stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressText
    }
}