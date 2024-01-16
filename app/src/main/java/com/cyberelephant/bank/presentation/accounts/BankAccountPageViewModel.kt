package com.cyberelephant.bank.presentation.accounts

import androidx.lifecycle.ViewModel
import com.cyberelephant.bank.domain.use_case.LoadAllBankAccountsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BankAccountPageViewModel(private val loadAllBankAccountsUseCase: LoadAllBankAccountsUseCase) :
    ViewModel() {

    fun loadAccounts(): Flow<BankAccountLoaded> =
        loadAllBankAccountsUseCase.call().map { BankAccountLoaded(it) }
}

sealed class BankAccountPageState

class BankAccountLoading : BankAccountPageState() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

class BankAccountLoaded(val bankAccounts: List<UiBankAccount>) : BankAccountPageState()
