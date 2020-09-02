package com.rogok.weatherforecast

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import com.rogok.weatherforecast.util.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val GOOGLE_PLACES_API_KEY = "AIzaSyDyF8XK99hz7CK_5c80Z77korpnnpz2BrQ"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
        APP_CONTEXT = APP_ACTIVITY.applicationContext
        val apiService = retrofit.create(OpenWeatherApiService::class.java)
        setAutocompleteSupportFragment(apiService)

        refresh_layout.setOnRefreshListener {
            getCurrentLocationWeather()
            refresh_layout.isRefreshing = false
            APP_ACTIVITY.weather_iv.visibility = View.VISIBLE

        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onStart() {
        super.onStart()
        //Getting the weather of current location
        getCurrentLocationWeather()

    }

    private fun getCurrentLocationWeather() {
        val currentLocation = CurrentLocation()
        val location = currentLocation.getLocation()
        location.addOnSuccessListener {

                val cityName = getCityName(it)
                //val cityName = currentLocation.getLocation()
                val autocompleteFragment =
                    APP_ACTIVITY.supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                            as AutocompleteSupportFragment
                autocompleteFragment.setText(cityName)
                updateLocationToolbar(cityName)
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

        }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show()
            Log.d("OnFailureListener", "Error trying to get last GPS location")
            it.printStackTrace()
        }
    }
}



