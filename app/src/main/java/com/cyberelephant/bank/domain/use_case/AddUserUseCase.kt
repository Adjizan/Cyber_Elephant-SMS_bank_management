package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository

class AddUserUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(param: AddUserParam) {
        bankAccountRepository.associatePhoneNumber(
            param.bankAccount,
            param.phoneNumber
        )
    }

}

data class AddUserParam private constructor(val bankAccount: String, val phoneNumber: String) {
    companion object {
        fun from(matchResult: MatchResult, phoneNumber: String): AddUserParam {
            return AddUserParam(matchResult.groups[1]!!.value, phoneNumber)
        }
    }
}
