package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository
import com.cyberelephant.bank.data.Command

class RequireHelpUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(phoneNumber: String): List<String> {
        val sealedSubclasses = Command::class.sealedSubclasses.map { it.objectInstance as Command }
        val isOrganizerAsking = bankAccountRepository.isOrganizer(phoneNumber)

        return sealedSubclasses.filter { it.forOrganizerOnly == isOrganizerAsking }
            .map { it.help }
    }

}