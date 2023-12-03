package com.cyberelephant.bank.core.di

import androidx.room.Room
import com.cyberelephant.bank.data.BankManagementDatabase
import com.cyberelephant.bank.domain.BankAccountRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val cyberElephantModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BankManagementDatabase::class.java,
            "cyber_elephant_bank_management"
        )
    }
    single { BankAccountRepositoryImpl(get()) }
}