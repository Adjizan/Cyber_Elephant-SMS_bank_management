package com.cyberelephant.bank.core.di

import androidx.room.Room
import com.cyberelephant.bank.SmsReceiver
import com.cyberelephant.bank.data.BankAccountDao
import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.BankManagementDatabase
import com.cyberelephant.bank.domain.repository.BankAccountRepositoryImpl
import com.cyberelephant.bank.domain.use_case.AssociatePhoneNumberUseCase
import com.cyberelephant.bank.domain.use_case.BadCommandUseCase
import com.cyberelephant.bank.domain.use_case.ClearAndImportBankAccountsUseCase
import com.cyberelephant.bank.domain.use_case.ConsultBalanceUseCase
import com.cyberelephant.bank.domain.use_case.CreateBankAccountUseCase
import com.cyberelephant.bank.domain.use_case.FundsTransferUseCase
import com.cyberelephant.bank.domain.use_case.LoadAllBankAccountsUseCase
import com.cyberelephant.bank.domain.use_case.RequireHelpUseCase
import com.cyberelephant.bank.domain.use_case.SaveReceivedSmsUseCase
import com.cyberelephant.bank.domain.use_case.SaveSentSmsUseCase
import com.cyberelephant.bank.domain.use_case.UpdateBankAccountUseCase
import com.cyberelephant.bank.domain.use_case.VerifyCommandUseCase
import com.cyberelephant.bank.presentation.MainPageViewModel
import com.cyberelephant.bank.presentation.accounts.BankAccountPageViewModel
import com.cyberelephant.bank.presentation.accounts.ModifyBankAccountViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cyberElephantModule = module {
    single<BankManagementDatabase> {
        Room.databaseBuilder(
            androidContext(), BankManagementDatabase::class.java, "cyber_elephant_bank_management"
        ).build()
    }

    single<BankAccountDao> { get<BankManagementDatabase>().getBankAccountDao() }

    single<BankAccountRepository> { BankAccountRepositoryImpl(get()) }

    single<LoadAllBankAccountsUseCase> { LoadAllBankAccountsUseCase(get()) }
    single<VerifyCommandUseCase> { VerifyCommandUseCase() }
    single<BadCommandUseCase> { BadCommandUseCase() }
    single<AssociatePhoneNumberUseCase> { AssociatePhoneNumberUseCase(get()) }
    single<CreateBankAccountUseCase> { CreateBankAccountUseCase(get()) }
    single<UpdateBankAccountUseCase> { UpdateBankAccountUseCase(get()) }
    single<ClearAndImportBankAccountsUseCase> { ClearAndImportBankAccountsUseCase(get()) }
    single<ConsultBalanceUseCase> { ConsultBalanceUseCase(get()) }
    single<FundsTransferUseCase> { FundsTransferUseCase(get()) }
    single<RequireHelpUseCase> { RequireHelpUseCase(get()) }
    single<SaveReceivedSmsUseCase> { SaveReceivedSmsUseCase(get()) }
    single<SaveSentSmsUseCase> { SaveSentSmsUseCase(get()) }

    single<SmsReceiver> {
        SmsReceiver()
    }
    viewModel<MainPageViewModel> { MainPageViewModel(get()) }
    viewModel<BankAccountPageViewModel> { BankAccountPageViewModel(get()) }
    viewModel<ModifyBankAccountViewModel> { ModifyBankAccountViewModel(get(), get()) }
}