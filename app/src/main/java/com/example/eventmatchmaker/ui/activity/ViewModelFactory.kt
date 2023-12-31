package com.example.eventmatchmaker.ui.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatchmaker.data.repository.UserRepository
import com.example.eventmatchmaker.di.Injection
import com.example.eventmatchmaker.ui.activity.addEvent.AddEventViewModel
import com.example.eventmatchmaker.ui.activity.main.MainViewModel
import com.example.eventmatchmaker.ui.activity.login.LoginViewModel
import com.example.eventmatchmaker.ui.activity.map.MapsViewModel
import com.example.eventmatchmaker.ui.activity.preference.PreferenceViewModel
import com.example.eventmatchmaker.ui.activity.profile.ProfileViewModel
import com.example.eventmatchmaker.ui.activity.search.SearchViewModel

class ViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddEventViewModel::class.java) -> {
                AddEventViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(PreferenceViewModel::class.java) -> {
                PreferenceViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideUserRepository(context)).also { INSTANCE = it }
            }
        }
    }
}