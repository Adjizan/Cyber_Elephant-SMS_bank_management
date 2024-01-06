package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository
import timber.log.Timber

class UpdateBankAccountUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(params: ModifyBankAccountParams): Boolean {
        return try {
            bankAccountRepository.updateBankAccount(
                accountNumber = params.accountNumber,
                name = params.name,
                balance = params.balance,
                phoneNumber = params.phoneNumber,
                isNPC = params.isNPC
            )
            true
        } catch (e: Exception) {
            Timber.e(e)
            false
        }

    }

}