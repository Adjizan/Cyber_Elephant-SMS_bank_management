package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BankAccountDao {
    @Query("SELECT * FROM BANK_ACCOUNT")
    fun allAccounts(): List<BankAccountEntity>
}