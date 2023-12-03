package com.cyberelephant.bank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bank_account")
data class BankAccountEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "current_balance") val currentBalance: Float,
)