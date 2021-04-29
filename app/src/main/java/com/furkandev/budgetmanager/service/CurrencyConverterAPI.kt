package com.furkandev.budgetmanager.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterAPI {

    @GET("latest")
    fun getData(@Query("base") base: String,@Query("symbols") symbols: String) : Call<Map<String, Any>>

}