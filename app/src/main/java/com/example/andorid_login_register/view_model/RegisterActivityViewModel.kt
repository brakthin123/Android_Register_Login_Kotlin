package com.example.andorid_login_register.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.andorid_login_register.data.RegisterBody
import com.example.andorid_login_register.data.User
import com.example.andorid_login_register.data.ValidateEmailBody
import com.example.andorid_login_register.repository.AuthRepository
import com.example.andorid_login_register.utils.RequestStatus
import kotlinx.coroutines.launch

class RegisterActivityViewModel(private val authRepository: AuthRepository, var application: Application) : ViewModel() {
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var isUniqueEmail: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var user: MutableLiveData<User> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getIsUniqueEmail(): LiveData<Boolean> = isUniqueEmail
    fun getUser(): LiveData<User> = user

    fun validateEmailAddress(body: ValidateEmailBody) {
       viewModelScope.launch {
           authRepository.validateEmailAddress(body).collect {
               when(it){
                   is RequestStatus.Waiting -> {
                       isLoading.value = true
                   }
                   is RequestStatus.Success -> {
                       isLoading.value = false
                       isUniqueEmail.value = it.data.isUnique
                   }
                   is RequestStatus.Error -> {
                       isLoading.value = false
                       errorMessage.value = it.message
                   }
               }
           }
       }
    }
    fun registerUser(body: RegisterBody){
        viewModelScope.launch {
            authRepository.registerUser(body).collect {
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        user.value = it.data.user
                        // save token using
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }
}