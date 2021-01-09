package com.hb.batapp.models

import com.google.gson.annotations.SerializedName

data class DailyWeatherResponse(
    @SerializedName("dt") val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TemperatureResponse,
    val feels_like: TemperatureResponse,
    val humidity: Int,
    val clouds: Int,
    val weather: List<WeatherResponse>
)

data class TemperatureResponse(
    val min: Double,
    val max: Double,
    val night: Double,
    val day: Double,
    val eve: Double,
    val morn: Double
)