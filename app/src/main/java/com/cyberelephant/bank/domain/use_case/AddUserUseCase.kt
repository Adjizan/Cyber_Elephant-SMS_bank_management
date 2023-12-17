package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository

class AddUserUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(bankAccount: String, phoneNumber: String) {
        bankAccountRepository.associatePhoneNumber(bankAccount, phoneNumber)
    }

}