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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class CurrentLocation(textView_location: TextView) {
    lateinit var lm: LocationManager
    lateinit var loc: Location


    fun checkPermission() : Location {
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
        lm = APP_ACTIVITY.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        var locList = object : LocationListener{
            override fun onLocationChanged(p0: Location?) {
                val address = reverseGeocode(p0)
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }

        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100.0f, locList)
        return loc
    }


    fun reverseGeocode(loc: Location?) {
        var geocoder = Geocoder(APP_ACTIVITY, Locale.getDefault())
        var addresses = geocoder.getFromLocation(loc!!.latitude, loc.longitude, 5)
        val address = addresses[0].locality   //return city name
        APP_ACTIVITY.textView_location.text = addresses[0].locality
            //"${address.locality}"

    }
}
