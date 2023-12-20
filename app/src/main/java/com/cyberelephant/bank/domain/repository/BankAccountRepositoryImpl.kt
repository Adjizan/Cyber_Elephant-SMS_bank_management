package com.cyberelephant.bank.domain.repository

import com.cyberelephant.bank.core.util.exception.BankAccountAlreadyLinked
import com.cyberelephant.bank.core.util.exception.BankAccountUnknown
import com.cyberelephant.bank.core.util.exception.InsufficientBalance
import com.cyberelephant.bank.core.util.exception.PhoneNumberUnknown
import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.TransferSuccessful

class BankAccountRepositoryImpl(private val bankAccountDao: BankAccountDao) :
    BankAccountRepository {
    override suspend fun allAccounts(): List<BankAccountEntity> = bankAccountDao.allAccounts()

    override suspend fun associatePhoneNumber(bankAccount: String, phoneNumber: String) {
        val entity = bankAccountDao.searchAccount(bankAccount)
        entity?.let {
            it.phoneNumber?.let { alreadyLinkedPhoneNumber ->
                throw BankAccountAlreadyLinked(bankAccount, alreadyLinkedPhoneNumber)
            } ?: run {
                bankAccountDao.updatePhoneNumber(bankAccount, phoneNumber)
            }
        } ?: run {
            throw BankAccountUnknown(bankAccount)
        }
    }

    override suspend fun consultBalanceFor(phoneNumber: String): Double =
        bankAccountDao.consultBalanceFor(phoneNumber)
            ?.let { return it }
            ?: run { throw PhoneNumberUnknown(phoneNumber) }

    override suspend fun transferFunds(
        fromAccount: String,
        destinationBankAccount: String,
        amount: Double,
        isNPC: Boolean
    ): TransferSuccessful {
        // this call is useless for NPC but verify the emitter phone number
        val consultBalanceFor = consultBalanceFor(fromAccount)
        if (isNPC || consultBalanceFor > amount) {
            bankAccountDao.searchAccount(destinationBankAccount)?.let {
                bankAccountDao.transferFunds(fromAccount, destinationBankAccount, amount)
                val name = bankAccountDao.searchAccount(fromAccount)!!.name
                val newBalance = consultBalanceFor(destinationBankAccount)
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