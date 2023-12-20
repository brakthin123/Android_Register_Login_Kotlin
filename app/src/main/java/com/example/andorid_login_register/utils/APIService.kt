package com.example.andorid_login_register.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIService {
    private const val BASE_URL = "https://boxify.item2.pro/"
    private var apiConsumer: APIConsumer? = null

    fun getService(): APIConsumer {
        if (apiConsumer == null) {
            synchronized(this) {
                if (apiConsumer == null) {
                    apiConsumer = buildRetrofit().create(APIConsumer::class.java)
                }
            }
        }
        return apiConsumer!!
    }

    private fun buildRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
