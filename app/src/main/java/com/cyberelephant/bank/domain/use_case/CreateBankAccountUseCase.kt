package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.core.util.debugLog
import com.cyberelephant.bank.data.BankAccountRepository

class CreateBankAccountUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(params: ModifyBankAccountParams): Boolean {
        return try {
            bankAccountRepository.createBankAccount(
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

data class ModifyBankAccountParams(
    val accountNumber: String,
    val name: String,
    val balance: Double,
    val phoneNumber: String?,
    val isOrga: Boolean
)