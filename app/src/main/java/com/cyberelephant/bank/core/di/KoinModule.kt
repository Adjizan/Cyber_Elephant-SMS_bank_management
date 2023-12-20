package com.cyberelephant.bank.core.di

import androidx.room.Room
import com.cyberelephant.bank.SmsReceiver
import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.BankManagementDatabase
import com.cyberelephant.bank.domain.repository.BankAccountRepositoryImpl
import com.cyberelephant.bank.domain.use_case.AddUserUseCase
import com.cyberelephant.bank.domain.use_case.BadCommandUseCase
import com.cyberelephant.bank.domain.use_case.ConsultBalanceUseCase
import com.cyberelephant.bank.domain.use_case.LoadAllBankAccountsUseCase
import com.cyberelephant.bank.domain.use_case.RequireHelpUseCase
import com.cyberelephant.bank.domain.use_case.TransferUseCase
import com.cyberelephant.bank.domain.use_case.VerifyCommandUseCase
import com.cyberelephant.bank.presentation.accounts.BankAccountPageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cyberElephantModule = module {
    single<BankManagementDatabase> {
        Room.databaseBuilder(
            androidContext(),
            BankManagementDatabase::class.java,
            "cyber_elephant_bank_management"
        ).build()
    }

    single<BankAccountDao> { get<BankManagementDatabase>().getBankAccountDao() }

    single<BankAccountRepository> { BankAccountRepositoryImpl(get()) }

    single<LoadAllBankAccountsUseCase> { LoadAllBankAccountsUseCase(get()) }
    single<VerifyCommandUseCase> { VerifyCommandUseCase() }
    single<BadCommandUseCase> { BadCommandUseCase() }
    single<AddUserUseCase> { AddUserUseCase(get()) }
    single<ConsultBalanceUseCase> { ConsultBalanceUseCase(get()) }
    single<TransferUseCase> { TransferUseCase(get()) }
    single<RequireHelpUseCase> { RequireHelpUseCase(get()) }

    single<SmsReceiver> {
        SmsReceiver()
    }
    viewModel<BankAccountPageViewModel> { BankAccountPageViewModel(get()) }
}