package com.rogok.weatherforecast.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.rogok.weatherforecast.data.repository.WeatherRepository
import com.rogok.weatherforecast.models.OpenWeatherLocation
import com.rogok.weatherforecast.ui.WeatherViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.java.KoinJavaComponent.inject
import java.util.*
import kotlin.coroutines.resume

interface LocationManager {

    suspend fun getLastLocation(): OpenWeatherLocation?
    suspend fun getCurrentLoc(): OpenWeatherLocation
    fun startLocation(result: (OpenWeatherLocation) -> Unit)
    fun stopLocation()
    fun getCityName(loc: OpenWeatherLocation?): String?
}

class LocationManagerImpl(
    private val context: Context,
    private val permissionManager: PermissionManager
) : LocationManager {

    override suspend fun getLastLocation(): OpenWeatherLocation? =
        suspendCancellableCoroutine { continuation ->
            permissionManager.checkLocationPermission(
                failure = {
                    continuation.resume(OpenWeatherLocation())
                }
            ) {
                LocationServices.getFusedLocationProviderClient(context).lastLocation
                    .addOnFailureListener {
                        continuation.resume(OpenWeatherLocation())
                    }
                    .addOnCanceledListener {
                        continuation.resume(OpenWeatherLocation())
                    }
                    .addOnSuccessListener { location ->
                        continuation.resume(
                            value = OpenWeatherLocation(
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                    }
            }
        }

    private var callback: LocationCallback? = null

    override fun startLocation(result: (OpenWeatherLocation) -> Unit) {
        stopLocation()
        permissionManager.checkLocationPermission(
            failure = {
                result(OpenWeatherLocation())
            }
        ) {
            LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(
                    LocationRequest.create()
                        .setInterval(10000)
                        .setFastestInterval(5000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY),
                    object : LocationCallback() {
                        override fun onLocationResult(p0: LocationResult?) {
                            super.onLocationResult(p0)
                            p0?.lastLocation?.let { location ->

                                result(
                                    OpenWeatherLocation(
                                        latitude = location.latitude,
                                        longitude = location.longitude
                                    )
                                )

                            } ?: result(OpenWeatherLocation())
                        }
                    }.apply {
                        callback = this
                    },
                    Looper.getMainLooper()
                )
        }
    }

    override fun stopLocation() {
        callback?.let {
            LocationServices.getFusedLocationProviderClient(context)
                .removeLocationUpdates(callback)
        }
    }

    override suspend fun getCurrentLoc(): OpenWeatherLocation =
        suspendCancellableCoroutine { continuation ->
            permissionManager.checkLocationPermission(
                failure = {
                    continuation.resume(OpenWeatherLocation())
                }
            ) {
                val cancel = CancellationTokenSource()
                LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY, cancel.token
                )
                    .addOnFailureListener {
                        continuation.resume(OpenWeatherLocation())
                    }
                    .addOnCanceledListener {
                        continuation.resume(OpenWeatherLocation())
                    }
                    .addOnSuccessListener {
                        continuation.resume(
                            OpenWeatherLocation(
                                longitude = it.longitude,
                                latitude = it.latitude
                            )
                        )
                    }
            }
        }

    override fun getCityName(loc: OpenWeatherLocation?): String? {
        loc?.let {
            val localityList = ArrayList<String?>()
            return try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 5)
                return if (addresses.isNullOrEmpty()) {
                    null
                } else {
                    var address: String?
                    addresses.forEach {

                        localityList.add(it.locality)
                    }
                    localityList[0]
                }
            } catch (e: Exception) {
                Log.d("Rogok_LocationManager", "getCityName:${e.message} ")
                ""
            }
        }
        return null
    }
}