package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class BankAccountDao {

    @Query("SELECT * FROM BANK_ACCOUNT ORDER BY name, is_organizer ASC")
    abstract suspend fun allAccounts(): List<BankAccountEntity>

    @Query("SELECT * FROM BANK_ACCOUNT WHERE accountNumber = :bankAccount")
    abstract suspend fun searchAccount(bankAccount: String): BankAccountEntity?

    @Query("UPDATE BANK_ACCOUNT SET phone_number = :phoneNumber WHERE accountNumber = :bankAccount")
    abstract suspend fun updatePhoneNumber(bankAccount: String, phoneNumber: String)

    @Query("SELECT current_balance FROM BANK_ACCOUNT WHERE phone_number = :phoneNumber")
    abstract suspend fun consultBalanceFor(phoneNumber: String): Double?

    @Query(
        """UPDATE bank_account 
                SET current_balance = current_balance + :amount 
                WHERE accountNumber = :bankAccount
        """
    )
    abstract suspend fun addFunds(bankAccount: String, amount: Double)

    @Query(
        """UPDATE bank_account 
                SET current_balance = current_balance - :amount 
                WHERE accountNumber = :bankAccount
        """
    )
    abstract suspend fun subtractFunds(bankAccount: String, amount: Double)

    @Transaction
    open suspend fun transferFunds(
        fromAccount: String,
        destinationBankAccount: String,
        amount: Double
    ) {
        addFunds(destinationBankAccount, amount)
        subtractFunds(fromAccount, amount)
    }

    @Query("SELECT is_organizer FROM bank_account WHERE phone_number = :phoneNumber")
    abstract fun isOrganizer(phoneNumber: String): Boolean?
}