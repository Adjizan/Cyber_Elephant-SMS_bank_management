package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BankAccountDao {

    @Query("SELECT * FROM BANK_ACCOUNT")
    suspend fun allAccounts(): List<BankAccountEntity>

    @Query("SELECT * FROM BANK_ACCOUNT WHERE accountNumber = :bankAccount")
    suspend fun searchAccount(bankAccount: String): BankAccountEntity?

    @Query("UPDATE BANK_ACCOUNT SET phoneNumber = :phoneNumber WHERE accountNumber = :bankAccount")
    suspend fun updatePhoneNumber(bankAccount: String, phoneNumber: String)
}