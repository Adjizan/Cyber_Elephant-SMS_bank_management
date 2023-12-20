package com.cyberelephant.bank.data

interface BankAccountRepository {
    suspend fun allAccounts(): List<BankAccountEntity>
    suspend fun associatePhoneNumber(bankAccount: String, phoneNumber: String)
    suspend fun consultBalanceFor(phoneNumber: String): Double
    suspend fun transferFunds(
        fromAccount: String,
        destinationBankAccount: String,
        amount: Double,
        isNPC: Boolean
    ): TransferSuccessful
}