package com.example.eventmatchmaker.ui.activity.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmatchmaker.data.model.UserModel
import com.example.eventmatchmaker.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val preferenceRepository: UserRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = preferenceRepository.login(email, password)
                if (response.user != null) {
                    val loginResult = response.user

                    val userModel = UserModel(
                        userId = loginResult.id ?: "",
                        name = loginResult.username ?: "",
                        email = email,
                        token = response.token ?: "",
                        isLogin = true
                    )
                    preferenceRepository.saveSession(userModel)
                    onSuccess.invoke()
                    isLoading.value = false
                } else {
//                    onError.invoke("Login failed: ${response.message ?: "Unknown error"}")
                    isLoading.value = false
                }
            } catch (e: Exception) {
                onError.invoke("Login failed: ${e.message ?: "Unknown error"}")
                isLoading.value = false
            }
        }
    }
}