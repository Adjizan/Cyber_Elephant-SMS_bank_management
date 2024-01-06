package com.cyberelephant.bank.data

import com.cyberelephant.bank.domain.use_case.ModifyBankAccountParams
import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun allAccounts(): Flow<List<BankAccountEntity>>
    suspend fun associatePhoneNumber(bankAccount: String, phoneNumber: String): String
    suspend fun consultBalanceForPhoneNumber(phoneNumber: String): Pair<String, Double>
    suspend fun pcTransferFunds(
        fromPhoneNumber: String,
        destinationBankAccount: String,
        amount: Double
    ): TransferSuccessful

    suspend fun isOrganizer(phoneNumber: String): Boolean
    suspend fun createBankAccount(newBankAccount: ModifyBankAccountParams)
    suspend fun clearAndImportAccounts(newBankAccounts: List<ModifyBankAccountParams>)

    suspend fun updateBankAccount(
        accountNumber: String,
        name: String,
        balance: Double,
        phoneNumber: String?,
        isNPC: Boolean
    )

    suspend fun npcTransferFunds(
        originatingBankAccountNumber: String,
        destinationBankAccountNumber: String,
        amount: Double,
    ): TransferSuccessful

    suspend fun wipeAll()
}