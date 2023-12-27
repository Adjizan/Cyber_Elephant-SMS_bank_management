package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository

class CreateBankAccountUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(params: CreateBankAccountParams): Boolean {
        return try {
            bankAccountRepository.createBankAccount(
                accountNumber = params.accountNumber,
                name = params.name,
                balance = params.balance,
                phoneNumber = params.phoneNumber,
                isOrga = params.isOrga
            )
            true
        } catch (e: Exception) {
            false
        }
    }

}

data class CreateBankAccountParams(
    val accountNumber: String,
    val name: String,
    val balance: Double,
    val phoneNumber: String?,
    val isOrga: Boolean
)