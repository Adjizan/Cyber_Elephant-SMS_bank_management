package com.cyberelephant.bank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bank_account")
data class BankAccountEntity(
    @PrimaryKey val accountNumber: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "current_balance") val currentBalance: Double = 0.0,
    @ColumnInfo(name = "organizer") val organizer: String
)