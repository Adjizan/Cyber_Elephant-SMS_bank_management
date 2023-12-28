package com.cyberelephant.bank.presentation.accounts

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.core.util.createRandomAccountNumber
import com.cyberelephant.bank.presentation.theme.BankManagementTheme
import com.cyberelephant.bank.presentation.theme.md_theme_dark_background
import com.cyberelephant.bank.presentation.theme.normalMargin
import com.cyberelephant.bank.presentation.theme.smallMargin
import com.cyberelephant.bank.presentation.theme.xSmallMargin

@Composable
fun BankAccountPage(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: BankAccountPageViewModel
) {

    val uiState by viewModel.loadAccounts().collectAsState(BankAccountLoading())

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
                items(count = bankAccounts.count(), key = { bankAccounts[it].name }) {
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
            .height(90.dp)
            .clickable {
                Toast
                    .makeText(current, "$uiBankAccount touched", Toast.LENGTH_SHORT)
                    .show()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = normalMargin),
            horizontalArrangement = Arrangement.spacedBy(smallMargin),
        ) {
            val balance = uiBankAccount.balance
            val balanceColor = when {
                balance > 0 -> colorResource(id = R.color.positive_balance)
                else -> colorResource(
                    id = R.color.negative_balance
                )
            }

            val subItemModifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .weight(1F, fill = true)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0F, fill = true),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = subItemModifier,
                    text = uiBankAccount.phoneNumber
                        ?: stringResource(id = R.string.account_cell_no_number),
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = subItemModifier,
                    text = uiBankAccount.name,
                    textAlign = TextAlign.Start
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0F, fill = true),
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    modifier = subItemModifier,
                    text = uiBankAccount.bankAccount,
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = subItemModifier,
                    text = "$balance",
                    color = balanceColor,
                    textAlign = TextAlign.End
                )
            }
        }
    }

}

class UiBankAccountPreviewProvider : PreviewParameterProvider<List<UiBankAccount>> {

    private val names: List<String> =
        listOf(
            "John Preston",
            "Kyle Reese",
            "Niobe",
            "Batou",
            "Roy Batty",
            "Kaneda",
            "Smith",
            "Nathan Never",
            "Spider Jerusalem",
            "JC Denton",
            "Quellcrist Falconer"
        )
    override val values: Sequence<List<UiBankAccount>>
        get() = sequenceOf((0..10).map {
            UiBankAccount(
                createRandomAccountNumber(),
                if (it % 2 == 0) {
                    "+3361234567$it"
                } else {
                    null
                },
                names[it],
                (-50000..50000).random().toDouble()
            )
        }.toList())

}


data class UiBankAccount(
    val bankAccount: String,
    val phoneNumber: String?,
    val name: String,
    val balance: Double
)
