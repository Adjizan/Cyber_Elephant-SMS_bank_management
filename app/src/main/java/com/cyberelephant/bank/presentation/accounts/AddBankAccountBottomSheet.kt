package com.cyberelephant.bank.presentation.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cyberelephant.bank.R
import com.cyberelephant.bank.core.util.ACCOUNT_NUMBER_LENGTH
import com.cyberelephant.bank.core.util.createRandomAccountNumber
import com.cyberelephant.bank.domain.use_case.CreateBankAccountParams
import com.cyberelephant.bank.presentation.theme.BankManagementTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBankAccountBottomSheet(
    viewModel: AddBankAccountViewModel,
    onDismiss: (() -> Unit)? = null,
    onValidate: (() -> Unit)? = null
) {
    val newAccountFormData = remember {
        mutableStateOf(
            NewAccountFormData(
                TextFieldValue(""),
                TextFieldValue(createRandomAccountNumber()),
                TextFieldValue("+33")
            )
        )
    }


    // TODO Error control and validation
    BankManagementTheme {

        ModalBottomSheet(
            onDismissRequest = { onDismiss?.invoke() },
            sheetState = rememberModalBottomSheetState(confirmValueChange = {
                false
            })
        ) {

            Column(
                modifier = Modifier
                    .height(400.dp)
                    .padding(16.dp)
            ) {
                Text(text = stringResource(R.string.add_account_sheet_title))
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
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
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
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                        singleLine = true,
                        isError = newAccountFormData.value.accountNumber.text.length != ACCOUNT_NUMBER_LENGTH
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newAccountFormData.value.phoneNumber,
                        onValueChange = {
                            newAccountFormData.value =
                                newAccountFormData.value.copy(phoneNumber = it)
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_phone_number_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newAccountFormData.value.balance,
                        onValueChange = {
                            if (Regex("^[-+]?\\d*$").matches(it.text)) {
                                newAccountFormData.value =
                                    newAccountFormData.value.copy(balance = it)
                            }
                        },
                        label = { Text(text = stringResource(R.string.add_account_sheet_balance_label)) },
                        singleLine = true,
                        placeholder = { Text(text = "0.0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                    TextButton(
                        onClick = { onDismiss?.invoke() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.generic_cancel))
                    }
                    TextButton(
                        onClick = {
                            viewModel.createBankAccount(newAccountFormData.value.toUseCaseParams())
                            onValidate?.invoke()
                            onDismiss?.invoke()
                        },
                        modifier = Modifier.padding(8.dp),
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
)

private fun NewAccountFormData.toUseCaseParams() = CreateBankAccountParams(
    name = name.text,
    accountNumber = accountNumber.text,
    phoneNumber = phoneNumber.text,
    balance = balance.text.toDouble(),
    isOrga = isOrga,
)