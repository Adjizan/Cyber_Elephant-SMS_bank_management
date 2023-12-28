package com.cyberelephant.bank.data

import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun allAccounts(): Flow<List<BankAccountEntity>>
    suspend fun associatePhoneNumber(bankAccount: String, phoneNumber: String): String
    suspend fun consultBalanceFor(phoneNumber: String): Pair<String, Double>
    suspend fun transferFunds(
        fromAccount: String,
        destinationBankAccount: String,
        amount: Double,
        isNPC: Boolean
    ): TransferSuccessful

    suspend fun isOrganizer(phoneNumber: String): Boolean
    suspend fun createBankAccount(
        accountNumber: String,
        name: String,
        balance: Double,
        phoneNumber: String?,
        isOrga: Boolean
    )

}