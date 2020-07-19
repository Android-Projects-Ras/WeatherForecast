package com.rogok.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

class MainActivity : AppCompatActivity() {

    private lateinit var urlForGlide: String



    //https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=439d4b804bc8187953eb36d2a8c26a02&lang=ru



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = retrofit.create(OpenWeatherApiService::class.java)

        apiService.getCurrentWeather("Запорожье", "ru").enqueue(object : Callback<CurrentWeatherResponse>{
            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Log.d("Andrew", "onFailure")
            }

            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                getIcon(response.body()!!.weather[0].id.toString())
                Glide.with(this@MainActivity)
                    .load(urlForGlide)
                    .into(weather_iv)
                getCurrentWeather(response)



                Log.d("Andrew", "onResponse: ${response.body()}")
            }

        })
    }

    private fun getCurrentWeather(response: Response<CurrentWeatherResponse>) {
        val description = response.body()!!.weather[0].description
        val temperature = (response.body()!!.main.temp).roundToInt()
        val maxTemperature = response.body()!!.main.tempMax
        val minTemperature = response.body()!!.main.tempMin
        val humidity = response.body()!!.main.humidity.toString()
        val windSpeed = (response.body()!!.wind.speed).roundToInt()

        textView_description.text = description
        text_view_temperature.text = "$temperature°C"
        textView_max_temperature.text = "MAX: $maxTemperature°C"
        textView_min_temperature.text = "MIN: $minTemperature°C"
        textView_humidity.text = "Влажность: $humidity%"
        textView_wind_speed.text = "Скорость ветра: $windSpeed м/с"

    }

    private fun getIcon(id: String) =
        when (id.toInt()) {
            in 200..232 -> urlForGlide = "http://openweathermap.org/img/wn/11d@2x.png"
            in 300..321 -> urlForGlide = "http://openweathermap.org/img/wn/09d@2x.png"
            in 500..504 -> urlForGlide = "http://openweathermap.org/img/wn/10d@2x.png"
            511 -> urlForGlide = "http://openweathermap.org/img/wn/13d@2x.png"
            in 520..531 -> urlForGlide = "http://openweathermap.org/img/wn/09d@2x.png"
            in 600..622 -> urlForGlide = "http://openweathermap.org/img/wn/13d@2x.png"
            in 701..781 -> urlForGlide = "http://openweathermap.org/img/wn/50d@2x.png"
            800 -> urlForGlide = "http://openweathermap.org/img/wn/01d@2x.png"
            in 701..781 -> urlForGlide = "http://openweathermap.org/img/wn/10d@2x.png"
            801 -> urlForGlide = "http://openweathermap.org/img/wn/02d@2x.png"
            802 -> urlForGlide = "http://openweathermap.org/img/wn/03d@2x.png"
            in 803..804 -> urlForGlide = "http://openweathermap.org/img/wn/04d@2x.png"
            else -> urlForGlide = "http://openweathermap.org/img/wn/01d@2x.png"

        }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
