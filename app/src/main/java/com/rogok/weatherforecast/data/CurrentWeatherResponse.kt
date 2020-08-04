package com.rogok.weatherforecast.data

data class CurrentWeatherResponse(

    val main: Main,
    val name: String,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
)