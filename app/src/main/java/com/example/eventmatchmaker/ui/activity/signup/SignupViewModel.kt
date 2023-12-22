package com.example.eventmatchmaker.ui.activity.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmatchmaker.data.repository.UserAccountRepository
import com.example.eventmatchmaker.data.retrofit.ApiServiceFactory
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserAccountRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    constructor() : this(UserAccountRepository(ApiServiceFactory.apiService))

    fun register(
        email: String,
        password: String,
        name: String,
        preferenceCategories: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(email, password, name, preferenceCategories)

                if (response.message.equals("User created successfully")) {
                    onSuccess.invoke()
                    isLoading.value = false
                } else {
                    onError.invoke("Registration failed: ${response.message}")
                    isLoading.value = false
                }
            } catch (e: Exception) {
                onError.invoke("Registration failed: ${e.message}")
                isLoading.value = false
            }
        }
    }
}