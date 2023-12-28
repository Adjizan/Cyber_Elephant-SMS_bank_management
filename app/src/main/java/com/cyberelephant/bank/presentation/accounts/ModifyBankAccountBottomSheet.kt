package com.cyberelephant.bank.presentation.accounts

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cyberelephant.bank.R
import com.cyberelephant.bank.core.util.ACCOUNT_NUMBER_LENGTH
import com.cyberelephant.bank.core.util.PHONE_NUMBER_PREFIX
import com.cyberelephant.bank.core.util.createRandomAccountNumber
import com.cyberelephant.bank.core.util.debugLog
import com.cyberelephant.bank.domain.use_case.ModifyBankAccountParams
import com.cyberelephant.bank.presentation.theme.BankManagementTheme
import com.cyberelephant.bank.presentation.theme.largeMargin
import com.cyberelephant.bank.presentation.theme.modalBottomSheet
import com.cyberelephant.bank.presentation.theme.smallMargin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyBankAccountBottomSheet(
    viewModel: ModifyBankAccountViewModel,
    bankAccount: UiBankAccount? = null,
    onDismiss: (() -> Unit)? = null,
    onValidate: ((Boolean?) -> Unit)? = null
) {

    BankManagementTheme {

        val currentContext = LocalContext.current

        ModalBottomSheet(
            onDismissRequest = { onDismiss?.invoke() },
            sheetState = rememberModalBottomSheetState(confirmValueChange = {
                false
            })
        ) {

            val currentAccountFormData = remember {

                mutableStateOf(bankAccount?.let {
                    CurrentAccountFormData.create(
                        it.name,
                        it.accountNumber,
                        it.phoneNumber,
                        it.balance,
                    )
                } ?: run {
                    CurrentAccountFormData.create(
                        "",
                        createRandomAccountNumber(),
                        PHONE_NUMBER_PREFIX,
                    )
                })
            }

            val coroutineScope = rememberCoroutineScope()

            Column(
                modifier = Modifier
                    .height(modalBottomSheet)
                    .fillMaxWidth()
                    .padding(horizontal = largeMargin)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(smallMargin),
                    text = stringResource(R.string.add_account_sheet_title),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1F),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = currentAccountFormData.value.name,
                        onValueChange = {
                            currentAccountFormData.value =
                                currentAccountFormData.value.copy(name = it)
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_name_label)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        isError = !currentAccountFormData.value.nameIsValid,
                        singleLine = true
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = currentAccountFormData.value.accountNumber,
                        onValueChange = {
                            currentAccountFormData.value =
                                currentAccountFormData.value.copy(accountNumber = it)
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_account_number_label)) },
                        supportingText = {
                            Text(
                                stringResource(
                                    R.string.add_account_sheet_account_number_help,
                                    ACCOUNT_NUMBER_LENGTH
                                )
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        isError = !currentAccountFormData.value.accountNumberIsValid
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = currentAccountFormData.value.phoneNumber,
                        onValueChange = {
                            if (Regex("^\\+?\\d*$").matches(it.text)) {
                                currentAccountFormData.value =
                                    currentAccountFormData.value.copy(phoneNumber = it)
                            }
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_phone_number_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                        ),
                        isError = !currentAccountFormData.value.phoneNumberIsValid
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = currentAccountFormData.value.balance,
                        onValueChange = {
                            if (Regex("^[-+]?\\d*[\\\\.,]?\\d*$").matches(it.text)) {
                                currentAccountFormData.value =
                                    currentAccountFormData.value.copy(balance = it)
                            }
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_balance_label)) },
                        singleLine = true,
                        placeholder = { Text(text = "0.0") },
                        keyboardActions = KeyboardActions(onSend = {
                            validateForm(
                                currentContext,
                                currentAccountFormData.value,
                                coroutineScope,
                                viewModel,
                                bankAccount,
                                onValidate,
                                onDismiss
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number, imeAction = ImeAction.Send
                        )
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = false, onCheckedChange = {
                            currentAccountFormData.value =
                                currentAccountFormData.value.copy(isNPC = it)
                        })
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically),
                            text = stringResource(R.string.add_account_sheet_is_orga_label),
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = { onDismiss?.invoke() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.generic_cancel))
                    }
                    Button(
                        modifier = Modifier.padding(8.dp), onClick = {
                            validateForm(
                                currentContext,
                                currentAccountFormData.value,
                                coroutineScope,
                                viewModel,
                                bankAccount,
                                onValidate,
                                onDismiss
                            )
                        }, enabled = currentAccountFormData.value.everythingSFine
                    ) {
                        Text(stringResource(R.string.add_account_sheet_validate_button_label))
                    }
                }
            }
        }
    }
}

private fun validateForm(
    currentContext: Context,
    currentAccountFormData: CurrentAccountFormData,
    coroutineScope: CoroutineScope,
    viewModel: ModifyBankAccountViewModel,
    bankAccount: UiBankAccount?,
    onValidate: ((Boolean?) -> Unit)?,
    onDismiss: (() -> Unit)?,
) {
    if (currentAccountFormData.everythingSFine) {

        coroutineScope.launch {
            viewModel.modifyBankAccount(
                params = currentAccountFormData.toUseCaseParams(),
                isNewAccount = bankAccount == null
            ).collect {
                if (it != null) {
                    Toast.makeText(
                        currentContext,
                        "Compte ${currentAccountFormData.accountNumber.text} MaJ",
                        Toast.LENGTH_SHORT
                    ).show()
                    onValidate?.invoke(it)
                    onDismiss?.invoke()
                }
            }
        }
    }
}

private data class CurrentAccountFormData private constructor(
    var name: TextFieldValue,
    var accountNumber: TextFieldValue,
    var phoneNumber: TextFieldValue,
    var balance: TextFieldValue,
    var isNPC: Boolean = false
) {
    val nameIsValid: Boolean
        get() = name.text.isNotEmpty()
    val accountNumberIsValid: Boolean
        get() = accountNumber.text.length == ACCOUNT_NUMBER_LENGTH

    val phoneNumberIsValid: Boolean
        get() = phoneNumber.text.isEmpty() || (phoneNumber.text.first() == '0' && phoneNumber.text.length == 10) || (phoneNumber.text.first() == '+' && phoneNumber.text.length == 12)

    val everythingSFine: Boolean
        get() = nameIsValid && accountNumberIsValid && phoneNumberIsValid

    companion object {
        fun create(
            name: String = "",
            accountNumber: String = "",
            phoneNumber: String? = null,
            balance: Double? = null,
            isNPC: Boolean = false
        ): CurrentAccountFormData {
            val formattedBalance = "%.2f".format(balance)
            return CurrentAccountFormData(
                name = TextFieldValue(name, selection = TextRange(name.length)),
                accountNumber = TextFieldValue(
                    accountNumber, selection = TextRange(accountNumber.length)
                ),
                phoneNumber = TextFieldValue(
                    phoneNumber ?: "", selection = TextRange(phoneNumber?.length ?: 0)
                ),
                balance = TextFieldValue(
                    formattedBalance, selection = TextRange(formattedBalance.length)
                ),
                isNPC = isNPC
            )
        }
    }
}

private fun CurrentAccountFormData.toUseCaseParams() = ModifyBankAccountParams(
    name = name.text.trim(),
    accountNumber = accountNumber.text.trim(),
    phoneNumber = phoneNumber.text.trim(),
    balance = try {
        balance.text.toDouble()
    } catch (e: NumberFormatException) {
        debugLog(exception = e)
        0.0
    },
    isOrga = isNPC,
)