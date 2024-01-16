package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository
import timber.log.Timber

class CreateBankAccountUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(params: ModifyBankAccountParams): Boolean {
        return try {
            bankAccountRepository.createBankAccount(params)
            true
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun call(params: List<ModifyBankAccountParams>): Boolean = params
        .map { call(it) }
        .all { it }

}

data class ModifyBankAccountParams(
    val accountNumber: String,
    val name: String,
    val balance: Double,
    val phoneNumber: String?,
    val isNPC: Boolean
)
