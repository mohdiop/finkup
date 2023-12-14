package com.mohdiop.finkup.database

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FinkRetrofit {
    companion object {
        private const val baseUrl = "http://10.0.2.2:6874"
        private val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(FinkApiController::class.java)

        fun getInstance(): FinkApiController {
            return retrofit
        }
    }
}