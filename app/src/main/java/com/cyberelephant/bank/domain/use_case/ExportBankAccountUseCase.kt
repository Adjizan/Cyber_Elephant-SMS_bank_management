package com.cyberelephant.bank.domain.use_case

import com.cyberelephant.bank.core.util.extension.toUi
import com.cyberelephant.bank.data.BankAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import java.io.OutputStream

class ExportBankAccountUseCase(private val bankAccountRepository: BankAccountRepository) {

    fun call(outputStream: OutputStream): Flow<Boolean> {
        return bankAccountRepository.allAccounts().map { bankAccountEntities ->
            val stringBuilder: StringBuilder = StringBuilder("#")
            stringBuilder.append(
                BankAccountCsvIndex.entries.joinToString(separator = ",") { it.columnName }
            )
            stringBuilder.appendLine()
            bankAccountEntities.toUi().forEach {
                stringBuilder.appendLine(it.toCsv())

            }
            outputStream.writer().use { it.write(stringBuilder.toString()) }
            true
        }
            .catch {
                Timber.e(it)
                emit(false)
            }
            .onCompletion { outputStream.close() }
            .flowOn(Dispatchers.IO)
    }

}