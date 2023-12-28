package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.core.util.debugLog
import com.cyberelephant.bank.data.BankAccountRepository

class UpdateBankAccountUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(params: ModifyBankAccountParams): Boolean {
        return try {
            bankAccountRepository.updateBankAccount(
                accountNumber = params.accountNumber,
                name = params.name,
                balance = params.balance,
                phoneNumber = params.phoneNumber,
                isNPC = params.isOrga
            )
            true
        } catch (e: Exception) {
            debugLog(exception = e)
            false
        }

    }

}