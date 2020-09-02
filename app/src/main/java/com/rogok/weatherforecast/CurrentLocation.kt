package com.rogok.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class CurrentLocation : LocationListener {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var lm: LocationManager

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                APP_ACTIVITY,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                APP_ACTIVITY,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                APP_ACTIVITY, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 111
            )
    }

    fun getLocation() : Task<Location?> {
        checkPermission()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(APP_ACTIVITY)
        val location = fusedLocationProviderClient.lastLocation
        return location
    }

    fun reverseGeocode(loc: Location?): String {
        val geocoder = Geocoder(APP_ACTIVITY, Locale.getDefault())
        val addresses = geocoder.getFromLocation(loc!!.latitude, loc.longitude, 1)
        val address = addresses[0].locality   //return city name
        return address
    }

    override fun onLocationChanged(p0: Location?) {
        reverseGeocode(p0)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }
}
