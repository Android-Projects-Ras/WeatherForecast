package com.rogok.weatherforecast.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.rogok.weatherforecast.APP_ACTIVITY
import com.rogok.weatherforecast.models.OpenWeatherLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

interface CurrentLocation {
    suspend fun getCurrentLocation()
}

class CurrentLocationImpl(
    private val context: Context
) : CurrentLocation {


    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    override suspend fun getCurrentLocation() {

        /*return suspendCancellableCoroutine { continuation ->
            if (checkPermission()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    continuation.resume(
                        value = OpenWeatherLocation(
                            longitude = it.longitude,
                            latitude = it.latitude
                        )
                    )
                }
            } else {
                requestPermission()
            }

        }*/

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            APP_ACTIVITY, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 1
        )
    }


}