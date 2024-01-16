package com.cyberelephant.bank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ReceivedSmsDao {

    @Insert
    abstract suspend fun insert(entity: ReceivedSmsEntity): Long

    @Query("SELECT * FROM received_sms")
    abstract suspend fun allReceivedSms(): List<ReceivedSmsEntity>
}
