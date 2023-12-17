package com.cyberelephant.bank.core.util.extension

import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.presentation.accounts.UiBankAccount

fun BankAccountEntity.toUi(): UiBankAccount = UiBankAccount(this.name, this.currentBalance)
fun List<BankAccountEntity>.toUi(): List<UiBankAccount> = map { it.toUi() }