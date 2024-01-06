package com.cyberelephant.bank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("received_sms")
data class ReceivedSmsEntity(
    @PrimaryKey(true) val receivedSmsId: Int = 0,
    @ColumnInfo("from_phone_number") val fromPhoneNumber: String,
    @ColumnInfo("message") val message: String,
    @ColumnInfo("received_at") val receivedAt: Long,
)