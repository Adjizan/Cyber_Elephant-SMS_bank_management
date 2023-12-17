package com.cyberelephant.bank.domain.repository

import com.cyberelephant.bank.core.util.exception.BankAccountAlreadyLinked
import com.cyberelephant.bank.core.util.exception.BankAccountUnknown
import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.data.BankAccountRepository

class BankAccountRepositoryImpl(private val bankAccountDao: BankAccountDao) :
    BankAccountRepository {
    override suspend fun allAccounts(): List<BankAccountEntity> = bankAccountDao.allAccounts()

    override suspend fun associatePhoneNumber(bankAccount: String, phoneNumber: String) {
        val entity = bankAccountDao.searchAccount(bankAccount)
        entity?.let {
            it.phoneNumber?.let { alreadyLinkedPhoneNumber ->
                throw BankAccountAlreadyLinked(alreadyLinkedPhoneNumber)
            } ?: run {
                bankAccountDao.updatePhoneNumber(bankAccount, phoneNumber)
            }
        } ?: run {
            throw BankAccountUnknown()
        }
    }
}