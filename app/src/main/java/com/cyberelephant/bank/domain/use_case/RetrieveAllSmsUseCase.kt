package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.CESms
import com.cyberelephant.bank.data.SmsRepository
import java.time.Instant

class RetrieveAllSmsUseCase(private val smsRepository: SmsRepository) {

    suspend fun call(): List<UiSms> = smsRepository.allSms().toUi()

}

data class UiSms(
    val phoneNumber: String,
    val message: String,
    val isIncoming: Boolean,
    val date: Instant
)

fun CESms.toUi(): UiSms = UiSms(
    phoneNumber = phoneNumber,
    message = message,
    isIncoming = isIncoming,
    date = date
)

fun List<CESms>.toUi(): List<UiSms> = map { it.toUi() }
