package com.cyberelephant.bank.data

interface BankAccountRepository {
    fun allAccounts(): List<BankAccountEntity>
}