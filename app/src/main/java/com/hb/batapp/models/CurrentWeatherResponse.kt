package com.hb.batapp.models

data class CurrentWeatherResponse(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val clouds: Int,
    val weather: List<WeatherResponse>
)
