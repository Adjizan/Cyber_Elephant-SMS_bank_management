package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.core.util.extension.toUi
import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.presentation.accounts.UiBankAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadAllBankAccountsUseCase(private val bankAccountRepository: BankAccountRepository) {

    fun call(): Flow<List<UiBankAccount>> {
        return bankAccountRepository.allAccounts().map { it.toUi() }
    }

}
