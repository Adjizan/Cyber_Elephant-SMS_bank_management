package com.cyberelephant.bank.data

data class TransferSuccessful(
    val fromName: String?,
    val newBalanceOriginatingAccount: Double,
    val newBalanceDestinationAccount: Double,
    val destinationPhoneNumber: String?
)
