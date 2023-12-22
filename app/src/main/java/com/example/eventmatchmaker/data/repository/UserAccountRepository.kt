package com.example.eventmatchmaker.data.repository

import com.example.eventmatchmaker.data.response.LoginResponse
import com.example.eventmatchmaker.data.response.RegisterResponse
import com.example.eventmatchmaker.data.retrofit.ApiService

class UserAccountRepository(private val apiService: ApiService) {

    suspend fun register(email: String, password: String, name: String, preferenceCategories: String): RegisterResponse {
        return apiService.
        register(email, password, name, preferenceCategories)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }
}