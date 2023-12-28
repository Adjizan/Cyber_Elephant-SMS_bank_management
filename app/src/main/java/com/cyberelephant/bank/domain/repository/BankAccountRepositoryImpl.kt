package com.cyberelephant.bank.domain.repository

import com.cyberelephant.bank.core.util.exception.BankAccountAlreadyLinked
import com.cyberelephant.bank.core.util.exception.BankAccountUnknown
import com.cyberelephant.bank.core.util.exception.InsufficientBalance
import com.cyberelephant.bank.core.util.exception.NotAnNPCBankAccount
import com.cyberelephant.bank.core.util.exception.PhoneNumberUnknown
import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.TransferSuccessful
import kotlinx.coroutines.flow.Flow

class BankAccountRepositoryImpl(private val bankAccountDao: BankAccountDao) :
    BankAccountRepository {
    override fun allAccounts(): Flow<List<BankAccountEntity>> = bankAccountDao.allAccounts()

    override suspend fun associatePhoneNumber(bankAccount: String, phoneNumber: String): String {
        searchAccountByNumber(bankAccount).phoneNumber?.let { alreadyLinkedPhoneNumber ->
            throw BankAccountAlreadyLinked(bankAccount, alreadyLinkedPhoneNumber)
        } ?: run {
            bankAccountDao.updatePhoneNumber(bankAccount, phoneNumber)
            return searchAccountByNumber(bankAccount).name
        }
    }

    override suspend fun consultBalanceForPhoneNumber(phoneNumber: String): Pair<String, Double> =
        bankAccountDao.searchAccountByPhone(phoneNumber)?.let {
            Pair(it.name, it.currentBalance)
        } ?: run { throw PhoneNumberUnknown(phoneNumber) }

    override suspend fun createBankAccount(
        accountNumber: String,
        name: String,
        balance: Double,
        phoneNumber: String?,
        isNPC: Boolean
    ) {
        bankAccountDao.insert(
            BankAccountEntity(
                accountNumber,
                phoneNumber,
                name,
                balance,
                isNPC
            )
        )
    }

    override suspend fun updateBankAccount(
        accountNumber: String,
        name: String,
        balance: Double,
        phoneNumber: String?,
        isNPC: Boolean
    ): Unit = bankAccountDao.updateBankAccount(accountNumber, phoneNumber, name, balance, isNPC)


    override suspend fun pcTransferFunds(
        fromPhoneNumber: String,
        destinationBankAccount: String,
        amount: Double
    ): TransferSuccessful {
        val bankAccount: BankAccountEntity = searchAccountByPhone(phoneNumber = fromPhoneNumber)

        if (bankAccount.currentBalance > amount) {
            val destination = searchAccountByNumber(destinationBankAccount)
            bankAccountDao.transferFunds(
                bankAccount.accountNumber,
                destination.accountNumber,
                amount
            )
            return TransferSuccessful(
                bankAccount.name,
                searchAccountByNumber(bankAccount.accountNumber).currentBalance,
                searchAccountByNumber(destination.accountNumber).currentBalance,
                destination.phoneNumber
            )
        } else {
            throw InsufficientBalance()
        }
    }

    override suspend fun npcTransferFunds(
        originatingBankAccountNumber: String,
        destinationBankAccountNumber: String,
        amount: Double
    ): TransferSuccessful {
        val bankAccount: BankAccountEntity = searchAccountByNumber(originatingBankAccountNumber)
        if (bankAccount.isOrganizer) {
            bankAccountDao.transferFunds(
                bankAccount.accountNumber,
                destinationBankAccountNumber,
                amount
            )
            val destinationBankAccount = searchAccountByNumber(destinationBankAccountNumber)
            return TransferSuccessful(
                bankAccount.name,
                searchAccountByNumber(originatingBankAccountNumber).currentBalance,
                destinationBankAccount.currentBalance,
                destinationBankAccount.phoneNumber
            )
        } else {
            throw NotAnNPCBankAccount(originatingBankAccountNumber)
        }
    }

    override suspend fun isOrganizer(phoneNumber: String): Boolean {
        return bankAccountDao.isOrganizer(phoneNumber)
            ?: run { throw PhoneNumberUnknown(phoneNumber) }
    }

    private suspend fun searchAccountByNumber(accountNumber: String): BankAccountEntity =
        bankAccountDao.searchAccount(accountNumber)
            ?: run { throw BankAccountUnknown(accountNumber) }

    private suspend fun searchAccountByPhone(phoneNumber: String): BankAccountEntity =
        bankAccountDao.searchAccountByPhone(phoneNumber)
            ?: run { throw PhoneNumberUnknown(phoneNumber) }
}