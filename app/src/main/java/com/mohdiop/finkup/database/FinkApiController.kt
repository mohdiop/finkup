package com.mohdiop.finkup.database

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface FinkApiController {

    @GET("/finks")
    fun getAllFinks(): Call<List<Fink>>

    @POST("/addFinks")
    fun addAllFinks(@Body finks: List<Fink>): Call<ResponseBody>

    @DELETE("/delete")
    fun deleteAllFinks(): Call<ResponseBody>
}