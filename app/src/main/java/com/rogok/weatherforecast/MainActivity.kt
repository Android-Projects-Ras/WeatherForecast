package com.rogok.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = OpenWeatherApiService()
        GlobalScope.launch(Dispatchers.Main) {
            val currentWeatherResponse =
                apiService.getCurrentWeather("Киев", "ru").await()
            weather_tv.text = currentWeatherResponse.toString()
        }
    }
}