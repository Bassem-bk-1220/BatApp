package com.hb.batapp.utils

import android.text.TextUtils

fun String.isValidEmail(): Boolean {
    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return EMAIL_REGEX.toRegex().matches(this)
}

fun String.isValidMobileNumber(): Boolean {
    return (length < 8 || TextUtils.isDigitsOnly(this))
}