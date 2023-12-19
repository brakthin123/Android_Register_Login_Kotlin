package com.example.andorid_login_register.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.andorid_login_register.data.User
import com.example.andorid_login_register.data.ValidateEmailBody
import com.example.andorid_login_register.repository.AuthRepository

class RegisterActivityViewModel(authRepository: AuthRepository, var application: Application): ViewModel() {
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var isUnique: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var user: MutableLiveData<User> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getIsUnique(): LiveData<Boolean> = isUnique
    fun getUser(): LiveData<User> = user

    fun validateEmailAddress(body: ValidateEmailBody){

    }
}