package com.example.eventmatchmaker.di

import android.content.Context
import com.example.eventmatchmaker.data.pref.UserPreference
import com.example.eventmatchmaker.data.pref.dataStore
import com.example.eventmatchmaker.data.repository.UserRepository
import com.example.eventmatchmaker.data.retrofit.ApiService
import com.example.eventmatchmaker.data.retrofit.ApiServiceFactory

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService: ApiService = ApiServiceFactory.apiService
        return UserRepository.getInstance(pref, apiService)
    }
}