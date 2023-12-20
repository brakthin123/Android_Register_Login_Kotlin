package com.example.andorid_login_register.data

data class RegisterResponse(
    val user: User,
    val token: String,
    val success: Boolean,
    val message: String
)
