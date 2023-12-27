package com.cyberelephant.bank.core.util


fun createRandomAccountNumber(): String =
    (0 until ACCOUNT_NUMBER_LENGTH).joinToString("") { ALPHANUMERIC_ARRAY.random().toString() }

const val ACCOUNT_NUMBER_LENGTH = 5
val ALPHANUMERIC_ARRAY = ('A'..'Z') + ('0'..'9')