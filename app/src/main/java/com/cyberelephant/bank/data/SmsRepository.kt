package com.cyberelephant.bank.data

import com.cyberelephant.bank.core.util.extension.toCESms
import java.time.Instant
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

    suspend fun allSms(): List<CESms> = allReceivedSms().toCESms()
        .plus(allSentSms().toCESms())
        .sortedByDescending { it.date }
}

data class CESms(
    val phoneNumber: String,
    val message: String,
    val isIncoming: Boolean,
    val date: Instant
)
