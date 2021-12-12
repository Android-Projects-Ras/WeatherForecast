package com.rogok.weatherforecast.models

import com.rogok.weatherforecast.data.Main
import com.rogok.weatherforecast.data.Sys
import com.rogok.weatherforecast.data.Weather
import com.rogok.weatherforecast.data.Wind

class CurrentWeather(
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind,
    val requestTime: Long?
)