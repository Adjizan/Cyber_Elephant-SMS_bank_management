package com.cyberelephant.bank.core.util

import android.util.Log
import com.cyberelephant.bank.BuildConfig

fun createRandomAccountNumber(): String =
    (0 until ACCOUNT_NUMBER_LENGTH).joinToString("") { ALPHANUMERIC_ARRAY.random().toString() }

const val ACCOUNT_NUMBER_LENGTH = 5
val ALPHANUMERIC_ARRAY = ('A'..'Z') + ('0'..'9')

const val PHONE_NUMBER_PREFIX = "+33"

fun debugLog(message: String? = null, exception: Exception? = null) {
    if (BuildConfig.DEBUG) {
        Log.e("CyberElephant - Bank Management", message, exception)
    }
}