package com.bassem.weathernow.api.models.apiWeekly

data class WeeklyxxWeather(
    val current: Current,
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)