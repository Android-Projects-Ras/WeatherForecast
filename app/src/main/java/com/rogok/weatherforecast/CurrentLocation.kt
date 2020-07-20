package com.rogok.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

class CurrentLocation () {
    private val APP_ACTIVITY: MainActivity = MainActivity()
    var PERMISSION_ID = 52


    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                APP_ACTIVITY,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                APP_ACTIVITY,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            APP_ACTIVITY,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager =
            APP_ACTIVITY.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }




}