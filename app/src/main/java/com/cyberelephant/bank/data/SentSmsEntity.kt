package com.cyberelephant.bank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("sent_sms")
data class SentSmsEntity(
    @PrimaryKey(true) val receivedSmsId: Int = 0,
    @ColumnInfo("to_phone_number") val toPhoneNumber: String,
    @ColumnInfo("message") val message: String,
    @ColumnInfo("sent_at") val sentAt: Long,
)
