package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository

class ConsultBalanceUseCase(private val bankAccountRepository: BankAccountRepository) {
    suspend fun call(phoneNumber: String): Pair<String, Double> {
        return bankAccountRepository.consultBalanceForPhoneNumber(phoneNumber)
    }
}