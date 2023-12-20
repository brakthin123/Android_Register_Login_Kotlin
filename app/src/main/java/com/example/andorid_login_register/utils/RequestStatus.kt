package com.example.andorid_login_register.utils

sealed class RequestStatus<out T> {
    data object Waiting : RequestStatus<Nothing>()
    data class Success<out T>(val data: T) : RequestStatus<T>()
    data class Error(val message: HashMap<String, String>) : RequestStatus<Nothing>()
}