package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.SmsRepository

class SaveSentSmsUseCase(private val smsRepository: SmsRepository) {
    suspend fun call(phoneNumber: String, message: String) {
        smsRepository.newOutgoingSms(phoneNumber = phoneNumber, message = message)
    }
}