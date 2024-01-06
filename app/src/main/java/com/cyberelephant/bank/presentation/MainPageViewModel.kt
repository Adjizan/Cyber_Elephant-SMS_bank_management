package com.cyberelephant.bank.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberelephant.bank.domain.use_case.ClearAndImportBankAccountsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.time.Duration.Companion.seconds

class MainPageViewModel(private val importBankAccountsUseCase: ClearAndImportBankAccountsUseCase) :
    ViewModel() {
    @OptIn(FlowPreview::class)
    fun importBankCSVData(toOpen: InputStream?): Flow<Boolean?> {

        val flow = MutableStateFlow<Boolean?>(null)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                flow.value = importBankAccountsUseCase.call(toOpen)
            }
        }

        return flow.timeout(5.seconds)

    }
}
