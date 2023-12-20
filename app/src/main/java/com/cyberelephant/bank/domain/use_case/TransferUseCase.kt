package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.TransferSuccessful

class TransferUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(param: TransferParam): TransferSuccessful {
        return bankAccountRepository.transferFunds(
            param.fromAccount,
            param.destinationBankAccount,
            param.amount,
            param.isNPC
        )
    }

}

data class TransferParam private constructor(
    val fromAccount: String,
    val destinationBankAccount: String,
    val amount: Double,
    val isNPC: Boolean
) {

    companion object {
        fun from(phoneNumber: String, message: MatchResult): TransferParam {
            return TransferParam(
                phoneNumber,
                message.groups[1]!!.value,
                message.groups[2]!!.value.replace(",", ".").toDouble(),
                isNPC = false
            )
        }

        fun fromNPC(phoneNumber: String, message: MatchResult): TransferParam {
            return TransferParam(
                phoneNumber,
                message.groups[2]!!.value,
                message.groups[3]!!.value.replace(",", ".").toDouble(),
                isNPC = true
            )
        }
    }

}