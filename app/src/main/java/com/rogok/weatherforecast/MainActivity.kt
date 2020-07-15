package com.rogok.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.gson.GsonConverterFactory.create

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

class MainActivity : AppCompatActivity() {



    //https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=439d4b804bc8187953eb36d2a8c26a02&lang=ru



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = retrofit.create(OpenWeatherApiService::class.java)

        apiService.getCurrentWeather("Kyiv", "ru").enqueue(object : Callback<CurrentWeatherResponse>{
            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Log.d("Andrew", "onFailure")
            }

            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                weather_tv.text = response.body().toString()
                //Log.d("Andrew", "onResponse: ${response.body()}")
            }

        })
        }


        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
