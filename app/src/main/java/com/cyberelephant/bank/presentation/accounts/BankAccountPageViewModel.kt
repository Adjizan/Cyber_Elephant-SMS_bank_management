package com.cyberelephant.bank.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberelephant.bank.domain.use_case.LoadAllBankAccountsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankAccountPageViewModel(private val loadAllBankAccountsUseCase: LoadAllBankAccountsUseCase) :
    ViewModel() {

    private val _uiState: MutableStateFlow<BankAccountPageState> =
        MutableStateFlow(BankAccountLoading())
    private val uiState: StateFlow<BankAccountPageState> = _uiState

    fun loadAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                BankAccountLoaded(bankAccounts = loadAllBankAccountsUseCase.call())
            }
        }
    }
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