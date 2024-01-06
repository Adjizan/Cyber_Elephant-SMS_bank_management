package com.cyberelephant.bank.data

import java.time.Instant.now

class SmsRepository(
    private val receivedSmsDao: ReceivedSmsDao,
    private val sentSmsDao: SentSmsDao
) {

    suspend fun newIncomingSms(phoneNumber: String, message: String) {
        receivedSmsDao.insert(
            ReceivedSmsEntity(
                fromPhoneNumber = phoneNumber,
                message = message,
                receivedAt = now().toEpochMilli()
            )
        )
    }

    suspend fun allReceivedSms(): List<ReceivedSmsEntity> {
        return receivedSmsDao.allReceivedSms()
    }

    suspend fun newOutgoingSms(phoneNumber: String, message: String) {
        sentSmsDao.insert(
            SentSmsEntity(
                toPhoneNumber = phoneNumber,
                message = message,
                sentAt = now().toEpochMilli()
            )
        )
    }

    suspend fun allSentSms(): List<SentSmsEntity> {
        return sentSmsDao.allSentSms()
    }

}