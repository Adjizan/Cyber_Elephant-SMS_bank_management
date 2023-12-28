package com.cyberelephant.bank.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberelephant.bank.core.util.PHONE_NUMBER_PREFIX
import com.cyberelephant.bank.domain.use_case.CreateBankAccountParams
import com.cyberelephant.bank.domain.use_case.CreateBankAccountUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class ModifyBankAccountViewModel(private val createBankAccountUseCase: CreateBankAccountUseCase) :
    ViewModel() {
    @OptIn(FlowPreview::class)
    fun createBankAccount(params: CreateBankAccountParams): Flow<Boolean?> {

        val toSave = params.phoneNumber?.let { phoneNumber ->
            if (phoneNumber.first() == '0') {
                params.copy(phoneNumber = phoneNumber.replaceFirstChar { "${PHONE_NUMBER_PREFIX}$it" })
            } else {
                params
            }
        } ?: params

        val flow = MutableStateFlow<Boolean?>(null)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    createBankAccountUseCase.call(toSave)
                    flow.value = true
                } catch (e: Exception) {
                    flow.value = false
                }
            }
        }

        return flow.timeout(5.seconds)
    }
}