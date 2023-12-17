package com.cyberelephant.bank.presentation.accounts

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavHostController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.presentation.theme.BankManagementTheme
import com.cyberelephant.bank.presentation.theme.md_theme_dark_background
import com.cyberelephant.bank.presentation.theme.xSmallMargin
import com.cyberelephant.bank.presentation.theme.xxLargeMargin

@Composable
fun BankAccountPage(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: BankAccountPageViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is BankAccountLoaded ->
            Box(modifier = modifier) {
                BankAccountsList((uiState as BankAccountLoaded).bankAccounts)
            }

        is BankAccountLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    }
}

@Preview
@Composable
private fun BankAccountsList(
    @PreviewParameter(UiBankAccountPreviewProvider::class) bankAccounts: List<UiBankAccount>
) {
    BankManagementTheme {
        Box(Modifier.background(md_theme_dark_background)) {
            LazyColumn() {
                items(count = bankAccounts.count(), key = { bankAccounts[it].accountNumber }) {
                    BankAccountRow(bankAccounts[it])
                }
            }
        }
    }
}

@Composable
fun BankAccountRow(uiBankAccount: UiBankAccount) {

    val current = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(xSmallMargin)
            .clickable {
                Toast
                    .makeText(current, "$uiBankAccount touched", Toast.LENGTH_SHORT)
                    .show()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(xxLargeMargin),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val balance = uiBankAccount.balance
            val balanceColor = when {
                balance > 0 -> colorResource(id = R.color.positive_balance)
                else -> colorResource(
                    id = R.color.negative_balance
                )
            }

            Text(text = uiBankAccount.accountNumber)
            uiBankAccount.phoneNumber?.let { Text(text = it) }
            uiBankAccount.name?.let { Text(text = it) }
            Text(text = "$balance", color = balanceColor)
        }
    }

}

class UiBankAccountPreviewProvider : PreviewParameterProvider<List<UiBankAccount>> {
    override val values: Sequence<List<UiBankAccount>>
        get() = sequenceOf((0..10).map {
            UiBankAccount(
                "$it",
                if (it % 2 == 0) {
                    "+336123456789$it"
                } else {
                    null
                },
                if (it % 3 == 0) {
                    "Pseudo $it"
                } else {
                    null
                },
                (-5000..5000).random().toDouble()
            )
        }.toList())

}


data class UiBankAccount(
    val accountNumber: String,
    val phoneNumber: String?,
    val name: String?,
    val balance: Double
)
