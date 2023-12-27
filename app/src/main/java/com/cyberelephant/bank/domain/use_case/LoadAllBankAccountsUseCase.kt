package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.core.util.extension.toUi
import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.presentation.accounts.UiBankAccount

class LoadAllBankAccountsUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(): List<UiBankAccount> {
        return bankAccountRepository.allAccounts().toUi()
    }

}
