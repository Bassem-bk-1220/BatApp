package com.hb.batapp.models

import com.google.gson.annotations.SerializedName

data class GeneralWeatherResponse(
    @SerializedName("lat") val latitude: Double?,
    @SerializedName("lon") val longitude: Double?,
    @SerializedName("timezone") val timeZone: String?,
    @SerializedName("current") val current: CurrentWeatherResponse?,
    @SerializedName("hourly") val hourly: List<CurrentWeatherResponse>?,
    @SerializedName("daily") val daily: List<DailyWeatherResponse>?
)

