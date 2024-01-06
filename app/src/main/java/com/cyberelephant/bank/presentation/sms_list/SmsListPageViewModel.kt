package com.cyberelephant.bank.presentation.sms_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberelephant.bank.domain.use_case.RetrieveAllSmsUseCase
import com.cyberelephant.bank.domain.use_case.UiSms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsListPageViewModel(private val retrieveAllSmsUseCase: RetrieveAllSmsUseCase) : ViewModel() {

    private val _smsFlow = MutableStateFlow<SmsListPageState>(SmsListLoading())
    val smsFlow: Flow<SmsListPageState>
        get() = _smsFlow.asStateFlow()
    private lateinit var allSms: List<UiSms>

    init {
        loadAllSms()
    }


    fun filterIncomingSms() {
        _smsFlow.value = SmsListLoaded(allSms.filter { it.isIncoming })
    }

    fun filterOutgoingSms() {
        _smsFlow.value = SmsListLoaded(allSms.filter { !it.isIncoming })
    }

    fun allSms() {
        _smsFlow.value = SmsListLoaded(allSms)
    }

    private fun loadAllSms() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allSms = retrieveAllSmsUseCase.call()
                _smsFlow.value = SmsListLoaded(allSms)
            }
        }
    }
}


sealed class SmsListPageState

class SmsListLoading : SmsListPageState() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

class SmsListLoaded(val smsList: List<UiSms>) : SmsListPageState()