package com.rogok.weatherforecast

import android.content.Context
import android.location.Location
import com.google.android.libraries.places.api.net.PlacesClient
import com.rogok.weatherforecast.models.OpenWeatherLocation
import com.rogok.weatherforecast.ui.MainActivity

lateinit var APP_ACTIVITY: MainActivity
lateinit var urlForGlide: String
lateinit var placesClient: PlacesClient

