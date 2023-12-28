package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BankAccountDao {

    @Query("SELECT * FROM BANK_ACCOUNT ORDER BY name, is_organizer ASC")
    abstract fun allAccounts(): Flow<List<BankAccountEntity>>

    @Query("SELECT * FROM BANK_ACCOUNT WHERE accountNumber = :bankAccount")
    abstract suspend fun searchAccount(bankAccount: String): BankAccountEntity?

    @Query("UPDATE BANK_ACCOUNT SET phone_number = :phoneNumber WHERE accountNumber = :bankAccount")
    abstract suspend fun updatePhoneNumber(bankAccount: String, phoneNumber: String)

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
        originatingBankAccount: String,
        destinationBankAccount: String,
        amount: Double
    ) {
        addFunds(destinationBankAccount, amount)
        subtractFunds(originatingBankAccount, amount)
    }

    @Query("SELECT is_organizer FROM bank_account WHERE phone_number = :phoneNumber")
    abstract suspend fun isOrganizer(phoneNumber: String): Boolean?

    @Insert
    abstract suspend fun insert(bankAccountEntity: BankAccountEntity)

    @Query(
        """
        UPDATE bank_account
            SET 
                name = :name,
                current_balance = :balance,
                phone_number = :phoneNumber,
                is_organizer = :orga
            WHERE
                accountNumber = :accountNumber
    """
    )
    abstract suspend fun updateBankAccount(
        accountNumber: String,
        phoneNumber: String?,
        name: String,
        balance: Double,
        orga: Boolean
    )

    @Query(
        """
        SELECT *
        FROM BANK_ACCOUNT
        WHERE
            phone_number = :phoneNumber
    """
    )
    abstract suspend fun searchAccountByPhone(phoneNumber: String): BankAccountEntity?

}