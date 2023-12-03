package com.cyberelephant.bank.presentation.accounts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BankAccountPageViewModel : ViewModel() {
    private val _uiState = mutableStateOf<BankAccountPageState>(BankAccountLoading())
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