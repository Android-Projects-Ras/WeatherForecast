package com.rogok.weatherforecast.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.rogok.weatherforecast.APP_ACTIVITY
import com.rogok.weatherforecast.R
import com.rogok.weatherforecast.data.Weather
import com.rogok.weatherforecast.data.repository.WeatherRepository
import com.rogok.weatherforecast.models.CurrentWeather
import com.rogok.weatherforecast.placesClient
import com.rogok.weatherforecast.ui.GOOGLE_PLACES_API_KEY
import com.rogok.weatherforecast.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

suspend fun setAutocompleteSupportFragment(
    //api: RetrofitInstance,
    weatherRepository: WeatherRepository
) {
    //APP_ACTIVITY.group_loading.visibility = View.VISIBLE

    if (!Places.isInitialized()) {
        Places.initialize(APP_ACTIVITY, GOOGLE_PLACES_API_KEY)
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

            //updateLocationToolbar("${place.name}")

            /*CoroutineScope(Dispatchers.IO).launch {
                weatherRepository.getDataFromDB()    //"${place.name}", "ru"
            }*/
            // Get info about the selected place.
            Log.i("onPlaceSelected", "Place: ${place.name}, ${place.id}")
        }

        override fun onError(p0: Status) {
            Log.i("onError", "An error occurred: $p0")
        }
    })
}

fun displayWeather(response: CurrentWeather?) {
    if (response != null) {
        val description = response.weather[0].description
        val temperature = (response.main.temp).roundToInt()
        val maxTemperature = response.main.tempMax
        val minTemperature = response.main.tempMin
        val humidity = response.main.humidity.toString()
        val windSpeed = (response.wind.speed).roundToInt()
        val cityName = response.name

        APP_ACTIVITY.binding.textViewDescription.text = description
        APP_ACTIVITY.binding.textViewTemperature.text = temperature.toString()
        APP_ACTIVITY.binding.textViewMaxTemperature.text = maxTemperature.toString()
        APP_ACTIVITY.binding.textViewMinTemperature.text = minTemperature.toString()
        APP_ACTIVITY.binding.textViewHumidity.text = humidity
        APP_ACTIVITY.binding.textViewWindSpeed.text = windSpeed.toString()
        APP_ACTIVITY.supportActionBar?.title = cityName
    }
}

fun loadImageFromCache(cachedIconName: Uri) {   //TODO:place for this fun
    Glide.with(APP_ACTIVITY)
        .load(cachedIconName)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(APP_ACTIVITY.binding.weatherIv)
}

fun setWeatherBackground(icon: String) {
    when (icon) {
        "01d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.clear)
        "01n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.clearskynight)
        "02d", "04d" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.fewcloudsday)
        "02n", "04n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.fewcloudsnight)
        "03d", "03n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.heavycloud)
        "09d", "09n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.showerrain)
        "10d", "10n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.rain)
        "11d", "11n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.thunderstorm)
        "13d", "13n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.snow)
        "50d", "50n" -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.mist)
        else -> APP_ACTIVITY.constraint_layout.setBackgroundResource(R.drawable.ic_sky)
    }
}

private fun changeToolbarColor(icon: String) {
    //TODO: implement fun
}


