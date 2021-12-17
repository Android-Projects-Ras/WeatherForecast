package com.rogok.weatherforecast.data.network

import com.rogok.weatherforecast.data.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}

//https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=00d9ac59b4bdc924164e509194b86c43&lang=ru

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"

interface OpenWeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") location: String = "Zaporizhia",
        @Query("lang") languageCode: String = "en",
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = API_KEY
    ): CurrentWeatherResponse
}