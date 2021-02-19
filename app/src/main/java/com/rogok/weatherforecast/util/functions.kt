package com.rogok.weatherforecast.util

import android.location.Geocoder
import android.location.Location
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.rogok.weatherforecast.*
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.roundToInt

fun setAutocompleteSupportFragment(apiService: OpenWeatherApiService) {
    APP_ACTIVITY.group_loading.visibility = View.VISIBLE

    if (!Places.isInitialized()) {
        Places.initialize(APP_CONTEXT, GOOGLE_PLACES_API_KEY)
    }
    placesClient = Places.createClient(APP_ACTIVITY)
    // Initialize the AutocompleteSupportFragment.

    val autocompleteFragment =
        APP_ACTIVITY.supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment

    // Specify the types of place data to return.
    autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

    // Set up a PlaceSelectionListener to handle the response.
    autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
        override fun onPlaceSelected(place: Place) {

            updateLocationToolbar("${place.name}")

            apiService.getCurrentWeather("${place.name}", "ru")
                .enqueue(object : Callback<CurrentWeatherResponse> {
                    override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                        Log.d("Andrew", "onFailure")
                    }

                    override fun onResponse(
                        call: Call<CurrentWeatherResponse>,
                        response: Response<CurrentWeatherResponse>
                    ) {
                        if (response.body() != null) {
                            getIcon(response.body()!!.weather[0].icon)
                            setWeatherBackground(response.body()!!.weather[0].icon)
                            Glide.with(APP_ACTIVITY)
                                .load(urlForGlide)
                                .into(APP_ACTIVITY.weather_iv)
                            getCurrentWeather(response)


                            Log.d("Andrew", "onResponse: ${response.body()}")
                        } else {
                            Toast.makeText(APP_ACTIVITY, APP_ACTIVITY.getString(R.string.no_such_place), Toast.LENGTH_SHORT)
                                .show()
                            autocompleteFragment.setText("")
                            updateLocationToolbar("")
                            APP_ACTIVITY.weather_iv.visibility = View.GONE
                            APP_ACTIVITY.textView_description.text = ""
                            APP_ACTIVITY.text_view_temperature.text = ""
                            APP_ACTIVITY.textView_max_temperature.text = ""
                            APP_ACTIVITY.textView_min_temperature.text = ""
                            APP_ACTIVITY.textView_humidity.text = ""
                            APP_ACTIVITY.textView_wind_speed.text = ""
                        }
                    }
                })

            // Get info about the selected place.
            Log.i("onPlaceSelected", "Place: ${place.name}, ${place.id}")
        }

        override fun onError(p0: Status) {
            Log.i("onError", "An error occurred: $p0")
        }
    })
}

fun getCurrentWeather(response: Response<CurrentWeatherResponse>?) {
    val description = response?.body()!!.weather[0].description
    val temperature = (response.body()!!.main.temp).roundToInt()
    val maxTemperature = response.body()!!.main.tempMax
    val minTemperature = response.body()!!.main.tempMin
    val humidity = response.body()!!.main.humidity.toString()
    val windSpeed = (response.body()!!.wind.speed).roundToInt()

    APP_ACTIVITY.textView_description.text = description
    APP_ACTIVITY.text_view_temperature.text = "$temperature°C"
    APP_ACTIVITY.textView_max_temperature.text = "MAX: $maxTemperature°C"
    APP_ACTIVITY.textView_min_temperature.text = "MIN: $minTemperature°C"
    APP_ACTIVITY.textView_humidity.text = "Влажность: $humidity%"
    APP_ACTIVITY.textView_wind_speed.text = "Скорость ветра: $windSpeed м/с"
    APP_ACTIVITY.group_loading.visibility = View.GONE



}

fun getIcon(icon: String) =
    when (icon) {
        "01d" -> urlForGlide = "http://openweathermap.org/img/wn/01d@2x.png"
        "01n" -> urlForGlide = "http://openweathermap.org/img/wn/01n@2x.png"
        "02d" -> urlForGlide = "http://openweathermap.org/img/wn/02d@2x.png"
        "02n" -> urlForGlide = "http://openweathermap.org/img/wn/02n@2x.png"
        "03d" -> urlForGlide = "http://openweathermap.org/img/wn/03d@2x.png"
        "03n" -> urlForGlide = "http://openweathermap.org/img/wn/03n@2x.png"
        "04d" -> urlForGlide = "http://openweathermap.org/img/wn/04d@2x.png"
        "04n" -> urlForGlide = "http://openweathermap.org/img/wn/04n@2x.png"
        "09d" -> urlForGlide = "http://openweathermap.org/img/wn/09d@2x.png"
        "09n" -> urlForGlide = "http://openweathermap.org/img/wn/09n@2x.png"
        "10d" -> urlForGlide = "http://openweathermap.org/img/wn/10d@2x.png"
        "10n" -> urlForGlide = "http://openweathermap.org/img/wn/10n@2x.png"
        "11d" -> urlForGlide = "http://openweathermap.org/img/wn/11d@2x.png"
        "11n" -> urlForGlide = "http://openweathermap.org/img/wn/11n@2x.png"
        "13d" -> urlForGlide = "http://openweathermap.org/img/wn/13d@2x.png"
        "13n" -> urlForGlide = "http://openweathermap.org/img/wn/13n@2x.png"
        "50d" -> urlForGlide = "http://openweathermap.org/img/wn/50d@2x.png"
        "50n" -> urlForGlide = "http://openweathermap.org/img/wn/50n@2x.png"
        else -> urlForGlide = "http://openweathermap.org/img/wn/01d@2x.png"
    }

fun setWeatherBackground(icon: String) {
    when (icon) {
        "01d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.clear)
        "01n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.clearskynight)
        "02d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.fewcloudsday)
        "02n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.fewcloudsnight)
        "03d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.heavycloud)
        "03n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.heavycloud)
        "04d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.fewcloudsday)
        "04n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.fewcloudsnight)
        "09d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.showerrain)
        "09n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.showerrain)
        "10d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.rain)
        "10n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.rain)
        "11d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.thunderstorm)
        "11n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.thunderstorm)
        "13d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.snow)
        "13n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.snow)
        "50d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.mist)
        "50n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.mist)
        else -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.ic_sky)
    }
}


fun updateLocationToolbar(location: String) {
    APP_ACTIVITY.supportActionBar?.title = location
}

fun getCityName(loc: Location?): String? {
    val geocoder = Geocoder(APP_ACTIVITY, Locale.getDefault())
    val addresses = loc?.latitude?.let { geocoder.getFromLocation(it, loc.longitude, 1) }
    val address = addresses?.get(0)?.locality   //return city name
    return address
}

