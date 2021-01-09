package com.hb.batapp.utils

import android.text.format.DateFormat
import android.view.View
import java.math.RoundingMode
import java.util.*

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Double.convertToCelcius(): String {
    return (this - 273.15).toBigDecimal().setScale(1, RoundingMode.UP).toString()
}


fun Long.convertTimeStampToDate(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000L
    return DateFormat.format("dd-MM-yyyy hh", cal).toString()
}