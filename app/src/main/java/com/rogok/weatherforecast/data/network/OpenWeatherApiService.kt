package com.rogok.weatherforecast.data.network

import com.rogok.weatherforecast.data.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}

//https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=439d4b804bc8187953eb36d2a8c26a02&lang=ru

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"

interface OpenWeatherApiService {

    @GET("weather")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en",
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = API_KEY
    ): Call<CurrentWeatherResponse>
}