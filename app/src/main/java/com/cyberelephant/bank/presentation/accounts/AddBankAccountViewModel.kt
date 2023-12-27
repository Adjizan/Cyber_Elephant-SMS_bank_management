package com.cyberelephant.bank.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberelephant.bank.domain.use_case.CreateBankAccountParams
import com.cyberelephant.bank.domain.use_case.CreateBankAccountUseCase
import kotlinx.coroutines.launch

class AddBankAccountViewModel(private val createBankAccountUseCase: CreateBankAccountUseCase) :
    ViewModel() {
    fun createBankAccount(params: CreateBankAccountParams) {
        viewModelScope.launch {
            createBankAccountUseCase.call(params)
        }
    }
}