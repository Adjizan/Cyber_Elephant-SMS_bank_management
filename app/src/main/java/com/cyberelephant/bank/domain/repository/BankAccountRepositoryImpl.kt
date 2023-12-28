package com.cyberelephant.bank.domain.repository

import com.cyberelephant.bank.core.util.exception.BankAccountAlreadyLinked
import com.cyberelephant.bank.core.util.exception.BankAccountUnknown
import com.cyberelephant.bank.core.util.exception.InsufficientBalance
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
        val entity = bankAccountDao.searchAccount(bankAccount)
        entity?.let {
            it.phoneNumber?.let { alreadyLinkedPhoneNumber ->
                throw BankAccountAlreadyLinked(bankAccount, alreadyLinkedPhoneNumber)
            } ?: run {
                bankAccountDao.updatePhoneNumber(bankAccount, phoneNumber)
                return bankAccountDao.searchAccount(bankAccount)!!.name
            }
        } ?: run {
            throw BankAccountUnknown(bankAccount)
        }
    }

    override suspend fun consultBalanceFor(phoneNumber: String): Pair<String, Double> =
        bankAccountDao.searchAccount(phoneNumber)
            ?.let { return Pair(it.name, it.currentBalance) }
            ?: run { throw PhoneNumberUnknown(phoneNumber) }

    override suspend fun createBankAccount(
        accountNumber: String,
        name: String,
        balance: Double,
        phoneNumber: String?,
        isOrga: Boolean
    ) {
        bankAccountDao.insert(
            BankAccountEntity(
                accountNumber,
                phoneNumber,
                name,
                balance,
                isOrga
            )
        )
    }

    override suspend fun transferFunds(
        fromAccount: String,
        destinationBankAccount: String,
        amount: Double,
        isNPC: Boolean
    ): TransferSuccessful {
        // this call is useless for NPC but verify the emitter phone number
        val consultBalanceFor = consultBalanceFor(fromAccount)
        if (isNPC || consultBalanceFor.second > amount) {
            bankAccountDao.searchAccount(destinationBankAccount)?.let {
                bankAccountDao.transferFunds(fromAccount, destinationBankAccount, amount)
                val name = bankAccountDao.searchAccount(fromAccount)!!.name
                val newBalance = consultBalanceFor(destinationBankAccount).second
                return TransferSuccessful(name, newBalance)
            }
                ?: run { throw BankAccountUnknown(destinationBankAccount) }
        } else {
            throw InsufficientBalance()
        }
    }

    override suspend fun isOrganizer(phoneNumber: String): Boolean {
        return bankAccountDao.isOrganizer(phoneNumber)
            ?: run { throw PhoneNumberUnknown(phoneNumber) }
    }
}