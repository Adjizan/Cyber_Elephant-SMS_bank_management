package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.data.BankAccountRepository
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ClearAndImportBankAccountsUseCase(private val bankAccountRepository: BankAccountRepository) {

    suspend fun call(toOpen: InputStream?): Boolean {
        return try {
            toOpen.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->

                    bankAccountRepository.clearAndImportAccounts(reader.lineSequence()
                        .filter { it.isNotBlank() }
                        .filter { !it.startsWith("#") }
                        .filter { it.count { toCount -> toCount == ',' } == BankAccountCsvIndex.entries.size - 1 }
                        .map {
                            val tokens = it.split(",")
                            ModifyBankAccountParams(
                                tokens[BankAccountCsvIndex.ACCOUNT_NUMBER_CSV_INDEX.ordinal],
                                tokens[BankAccountCsvIndex.NAME_CSV_INDEX.ordinal],
                                tokens[BankAccountCsvIndex.BALANCE_CSV_INDEX.ordinal].toDouble(),
                                tokens[BankAccountCsvIndex.PHONE_NUMBER_CSV_INDEX.ordinal],
                                tokens[BankAccountCsvIndex.NPC_CSV_INDEX.ordinal].toBoolean(),
                            )
                        }.toList()
                    )
                }
            }
            true
        } catch (e: Exception) {
            Timber.e(e)
            false
        }

    }

}

enum class BankAccountCsvIndex {
    ACCOUNT_NUMBER_CSV_INDEX,
    NAME_CSV_INDEX,
    BALANCE_CSV_INDEX,
    PHONE_NUMBER_CSV_INDEX,
    NPC_CSV_INDEX,
}