package com.hb.batapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.LocationServices
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun isGpsEnabled(activity: Activity): Boolean {
    val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}


fun openDialog(
    activity: Activity,
    title: String,
    message: String,
    positiveMessage: String,
    negativeMessage: String,
    positiveAction: Unit
) {
    with(AlertDialog.Builder(activity)) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveMessage) { dialog, which ->
            positiveAction
        }
        setNegativeButton(negativeMessage) { dialog, which ->
            dialog.dismiss()
        }
        create()
        setCancelable(false)
        show()
    }

}


/**
 * suspend fun that return the current location of the device
 * @param activity
 * @return current location
 */
@SuppressLint("MissingPermission")
suspend fun getDeviceLocation(activity: Activity): Location? = suspendCoroutine { sc ->
    val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    val location = mFusedLocationProviderClient.lastLocation
    location.addOnCompleteListener { task ->
        sc.resume(task.result)
    }.addOnFailureListener {
        sc.resume(null)
    }
}