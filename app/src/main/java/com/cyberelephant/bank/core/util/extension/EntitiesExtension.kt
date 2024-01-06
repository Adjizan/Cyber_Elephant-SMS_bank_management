package com.cyberelephant.bank.core.util.extension

import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.domain.use_case.ModifyBankAccountParams
import com.cyberelephant.bank.presentation.accounts.UiBankAccount

fun ModifyBankAccountParams.toEntity(): BankAccountEntity = BankAccountEntity(
    this.accountNumber,
    this.phoneNumber,
    this.name,
    this.balance,
    this.isNPC
)

fun List<ModifyBankAccountParams>.toEntities(): List<BankAccountEntity> = map {
    it.toEntity()
}

fun BankAccountEntity.toUi(): UiBankAccount = UiBankAccount(
    this.accountNumber,
    this.phoneNumber,
    this.name,
    this.currentBalance
)

fun List<BankAccountEntity>.toUi(): List<UiBankAccount> = map { it.toUi() }