package com.cyberelephant.bank.domain.repository

import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.data.BankAccountRepository

class BankAccountRepositoryImpl(private val bankAccountDao: BankAccountDao) :
    BankAccountRepository {
    override fun allAccounts(): List<BankAccountEntity> {
        bankAccountDao.allAccounts()
        // TODO remove test code
        return (0..50).map {
            BankAccountEntity(it, "Pseudo $it", (-5000..5000).random().toDouble())
        }.toList()
    }
}