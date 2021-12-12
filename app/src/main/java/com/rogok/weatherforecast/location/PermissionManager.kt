package com.rogok.weatherforecast.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rogok.weatherforecast.models.OpenWeatherLocation

interface PermissionManager {
    fun checkLocationPermission(
        failure: () -> Unit,
        success: () -> Unit
    )
}

class PermissionManagerImpl(private val context: Context) : PermissionManager {

    override fun checkLocationPermission(failure: () -> Unit, success: () -> Unit) =
        Dexter.withContext(context)
            .withPermissions(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let {
                        if (p0.areAllPermissionsGranted()) {
                            success()
                        } else {
                            failure()
                        }
                    } ?: failure()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest() ?: failure()
                }

            })
            .withErrorListener {
                failure()
            }
            .check()
}