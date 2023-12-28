package com.cyberelephant.bank.presentation.accounts

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
import com.cyberelephant.bank.domain.use_case.CreateBankAccountParams
import com.cyberelephant.bank.presentation.theme.BankManagementTheme
import com.cyberelephant.bank.presentation.theme.largeMargin
import com.cyberelephant.bank.presentation.theme.modalBottomSheet
import com.cyberelephant.bank.presentation.theme.smallMargin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyBankAccountBottomSheet(
    viewModel: ModifyBankAccountViewModel,
    onDismiss: (() -> Unit)? = null,
    onValidate: ((Boolean?) -> Unit)? = null
) {

    BankManagementTheme {

        ModalBottomSheet(
            onDismissRequest = { onDismiss?.invoke() },
            sheetState = rememberModalBottomSheetState(confirmValueChange = {
                false
            })
        ) {

            val newAccountFormData = remember {
                val accountNumber = createRandomAccountNumber()
                mutableStateOf(
                    NewAccountFormData(
                        TextFieldValue(""),
                        TextFieldValue(accountNumber, selection = TextRange(accountNumber.length)),
                        TextFieldValue(
                            PHONE_NUMBER_PREFIX, selection = TextRange(PHONE_NUMBER_PREFIX.length)
                        )
                    )
                )
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
                        value = newAccountFormData.value.name,
                        onValueChange = {
                            newAccountFormData.value = newAccountFormData.value.copy(name = it)
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_name_label)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        isError = !newAccountFormData.value.nameIsValid,
                        singleLine = true
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newAccountFormData.value.accountNumber,
                        onValueChange = {
                            newAccountFormData.value =
                                newAccountFormData.value.copy(accountNumber = it)
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
                        isError = !newAccountFormData.value.accountNumberIsValid
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newAccountFormData.value.phoneNumber,
                        onValueChange = {
                            if (Regex("^\\+?\\d*$").matches(it.text)) {
                                newAccountFormData.value =
                                    newAccountFormData.value.copy(phoneNumber = it)
                            }
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_phone_number_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                        ),
                        isError = !newAccountFormData.value.phoneNumberIsValid
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newAccountFormData.value.balance,
                        onValueChange = {
                            if (Regex("^[-+]?\\d*[\\\\.,]?\\d*$").matches(it.text)) {
                                newAccountFormData.value =
                                    newAccountFormData.value.copy(balance = it)
                            }
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_balance_label)) },
                        singleLine = true,
                        placeholder = { Text(text = "0.0") },
                        keyboardActions = KeyboardActions(onDone = {
                            if (newAccountFormData.value.everythingSFine) {
                                viewModel.createBankAccount(newAccountFormData.value.toUseCaseParams())
                            }
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number, imeAction = ImeAction.Send
                        )
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Checkbox(checked = false, onCheckedChange = {
                            newAccountFormData.value = newAccountFormData.value.copy(isOrga = it)
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
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            coroutineScope.launch {
                                viewModel.createBankAccount(newAccountFormData.value.toUseCaseParams())
                                    .collect {
                                        onValidate?.invoke(it)
                                    }
                            }
                            onDismiss?.invoke()
                        },
                        enabled = newAccountFormData.value.everythingSFine
                    ) {
                        Text(stringResource(R.string.add_account_sheet_validate_button_label))
                    }
                }
            }
        }
    }
}

private data class NewAccountFormData(
    var name: TextFieldValue,
    var accountNumber: TextFieldValue,
    var phoneNumber: TextFieldValue = TextFieldValue(),
    var balance: TextFieldValue = TextFieldValue(),
    var isOrga: Boolean = false
) {
    val nameIsValid: Boolean
        get() = name.text.isNotEmpty()
    val accountNumberIsValid: Boolean
        get() = accountNumber.text.length == ACCOUNT_NUMBER_LENGTH

    val phoneNumberIsValid: Boolean
        get() = phoneNumber.text.isEmpty()
                || (phoneNumber.text.first() == '0' && phoneNumber.text.length == 10)
                || (phoneNumber.text.first() == '+' && phoneNumber.text.length == 12)

    val everythingSFine: Boolean
        get() = nameIsValid && accountNumberIsValid && phoneNumberIsValid
}

private fun NewAccountFormData.toUseCaseParams() = CreateBankAccountParams(
    name = name.text.trim(),
    accountNumber = accountNumber.text.trim(),
    phoneNumber = phoneNumber.text.trim(),
    balance = try {
        balance.text.toDouble()
    } catch (e: NumberFormatException) {
        0.0
    },
    isOrga = isOrga,
)