package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.TransferSuccessful

class FundsTransferUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(params: FundsTransferParam): TransferSuccessful {

        if (params.originatingPhoneNumber != null && params.originatingBankAccount != null) {
            throw IllegalArgumentException("Can't be a originating phone number AND bank account for funds transfer")
        }
        return if (params.originatingPhoneNumber != null) {
            bankAccountRepository.pcTransferFunds(
                params.originatingPhoneNumber,
                params.destinationBankAccount,
                params.amount
            )
        } else {
            return bankAccountRepository.npcTransferFunds(
                params.originatingBankAccount!!,
                params.destinationBankAccount,
                params.amount
            )

        }

    }

}

data class FundsTransferParam private constructor(
    val originatingPhoneNumber: String?,
    val originatingBankAccount: String?,
    val destinationBankAccount: String,
    val amount: Double

) {
    companion object {
        fun fromPC(phoneNumber: String, message: MatchResult): FundsTransferParam {
            return FundsTransferParam(
                originatingPhoneNumber = phoneNumber,
                originatingBankAccount = null,
                destinationBankAccount = message.groups[1]!!.value,
                amount = message.groups[2]!!.value.replace(",", ".").toDouble(),
            )
        }

        fun fromNPC(message: MatchResult): FundsTransferParam {
            return FundsTransferParam(
                originatingPhoneNumber = null,
                originatingBankAccount = message.groups[1]!!.value,
                destinationBankAccount = message.groups[2]!!.value,
                amount = message.groups[3]!!.value.replace(",", ".").toDouble(),
            )
        }
    }
}
