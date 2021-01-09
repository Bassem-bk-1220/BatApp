package com.hb.batapp.network

import com.hb.batapp.models.GeneralWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appId") appID: String
    ): GeneralWeatherResponse
}