package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository

class ConsultBalanceUseCase(private val bankAccountRepository: BankAccountRepository) {
    suspend fun call(phoneNumber: String): Double {
        return bankAccountRepository.consultBalanceFor(phoneNumber)
    }
}