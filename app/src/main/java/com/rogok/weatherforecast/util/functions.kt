package com.rogok.weatherforecast.util

import android.location.Geocoder
import android.location.Location
import android.util.Log
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
                        getIcon(response.body()!!.weather[0].id.toString())
                        Glide.with(APP_ACTIVITY)
                            .load(urlForGlide)
                            .into(APP_ACTIVITY.weather_iv)
                        getCurrentWeather(response)

                        Log.d("Andrew", "onResponse: ${response.body()}")
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

fun getCurrentWeather(response: Response<CurrentWeatherResponse>) {
    val description = response.body()!!.weather[0].description
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


}

fun getIcon(id: String) =
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

fun updateLocationToolbar(location: String) {
    APP_ACTIVITY.supportActionBar?.title = location
}

fun getCityName(loc: Location?) : String {
    var geocoder = Geocoder(APP_ACTIVITY, Locale.getDefault())
    var addresses = geocoder.getFromLocation(loc!!.latitude, loc.longitude, 5)
    val address = addresses[0].locality   //return city name
    APP_ACTIVITY.textView_location.text = addresses[0].locality
    return address
}