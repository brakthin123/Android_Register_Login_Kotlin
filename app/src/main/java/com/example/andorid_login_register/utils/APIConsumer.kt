package com.example.andorid_login_register.utils

import com.example.andorid_login_register.data.UniqueEmailValidationResponse
import com.example.andorid_login_register.data.ValidateEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    @POST("")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody) : Response<UniqueEmailValidationResponse>
}