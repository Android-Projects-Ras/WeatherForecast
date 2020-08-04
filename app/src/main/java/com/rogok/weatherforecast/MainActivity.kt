package com.rogok.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.net.PlacesClient
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import com.rogok.weatherforecast.util.getCityName
import com.rogok.weatherforecast.util.getCurrentWeather
import com.rogok.weatherforecast.util.getIcon
import com.rogok.weatherforecast.util.setAutocompleteSupportFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val GOOGLE_PLACES_API_KEY = "AIzaSyDyF8XK99hz7CK_5c80Z77korpnnpz2BrQ"

lateinit var urlForGlide: String
lateinit var placesClient: PlacesClient
lateinit var apiService: OpenWeatherApiService


class MainActivity : AppCompatActivity() {


    //https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=439d4b804bc8187953eb36d2a8c26a02&lang=ru

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
        APP_CONTEXT = APP_ACTIVITY.applicationContext
        val apiService = retrofit.create(OpenWeatherApiService::class.java)
        setAutocompleteSupportFragment(apiService)

    }

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    override fun onStart() {
        super.onStart()
        val currentLocation = CurrentLocation(APP_ACTIVITY.textView_location)
        val location = currentLocation.checkPermission()
        val cityName = getCityName(location)
        val apiService = retrofit.create(OpenWeatherApiService::class.java)
        apiService.getCurrentWeather(cityName, "ru")
            .enqueue(object : Callback<CurrentWeatherResponse> {
                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    Log.d("Andrew", "onFailure")
                }

                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>
                ) {
                    getIcon(response.body()!!.weather[0].id.toString())
                    Glide.with(APP_ACTIVITY)
                        .load(urlForGlide)
                        .into(APP_ACTIVITY.weather_iv)
                    getCurrentWeather(response)

                    Log.d("Andrew", "onResponse: ${response.body()}")
                }

            })

    }
}



