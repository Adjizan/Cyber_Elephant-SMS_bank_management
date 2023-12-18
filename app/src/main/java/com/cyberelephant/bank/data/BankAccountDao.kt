package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BankAccountDao {

    @Query("SELECT * FROM BANK_ACCOUNT ORDER BY name, is_organizer ASC")
    suspend fun allAccounts(): List<BankAccountEntity>

    @Query("SELECT * FROM BANK_ACCOUNT WHERE accountNumber = :bankAccount")
    suspend fun searchAccount(bankAccount: String): BankAccountEntity?

    @Query("UPDATE BANK_ACCOUNT SET phone_number = :phoneNumber WHERE accountNumber = :bankAccount")
    suspend fun updatePhoneNumber(bankAccount: String, phoneNumber: String)

    @Query("SELECT current_balance FROM BANK_ACCOUNT WHERE phone_number = :phoneNumber")
    suspend fun consultBalanceFor(phoneNumber: String): Double?
}