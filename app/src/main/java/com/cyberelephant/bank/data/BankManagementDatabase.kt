package com.cyberelephant.bank.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BankAccountEntity::class, ReceivedSmsEntity::class, SentSmsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BankManagementDatabase : RoomDatabase() {

    abstract fun getBankAccountDao(): BankAccountDao
    abstract fun getReceivedSmsDao(): ReceivedSmsDao
    abstract fun getSentSmsDao(): SentSmsDao

}
