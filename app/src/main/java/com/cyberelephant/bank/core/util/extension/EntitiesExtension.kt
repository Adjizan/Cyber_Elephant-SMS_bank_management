package com.cyberelephant.bank.core.util.extension

import com.cyberelephant.bank.data.BankAccountEntity
import com.cyberelephant.bank.data.CESms
import com.cyberelephant.bank.data.ReceivedSmsEntity
import com.cyberelephant.bank.data.SentSmsEntity
import com.cyberelephant.bank.domain.use_case.ModifyBankAccountParams
import com.cyberelephant.bank.presentation.accounts.UiBankAccount
import java.time.Instant

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

fun SentSmsEntity.toCESms(): CESms = CESms(
    phoneNumber = toPhoneNumber,
    message = message,
    date = Instant.ofEpochMilli(this.sentAt),
    isIncoming = false
)

@JvmName("sentSmsToCESms")
fun List<SentSmsEntity>.toCESms(): List<CESms> = map { it.toCESms() }

fun ReceivedSmsEntity.toCESms(): CESms = CESms(
    phoneNumber = fromPhoneNumber,
    message = message,
    date = Instant.ofEpochMilli(this.receivedAt),
    isIncoming = false
)

@JvmName("receivedSmsToCESms")
fun List<ReceivedSmsEntity>.toCESms(): List<CESms> = map { it.toCESms() }