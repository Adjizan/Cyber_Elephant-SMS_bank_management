package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class SentSmsDao {

    @Insert
    abstract suspend fun insert(entity: SentSmsEntity): Long

    @Query("SELECT * FROM sent_sms")
    abstract suspend fun allSentSms(): List<SentSmsEntity>
}
