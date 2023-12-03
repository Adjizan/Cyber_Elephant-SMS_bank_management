package com.cyberelephant.bank.domain

import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountRepository

class BankAccountRepositoryImpl(private val bankAccountDao: BankAccountDao) :
    BankAccountRepository {
}