package com.example.eventmatchmaker.data.model

data class UserModel(
    val userId: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)