package com.cyberelephant.bank.presentation.accounts

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavHostController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.core.util.createRandomAccountNumber
import com.cyberelephant.bank.presentation.theme.BankManagementTheme
import com.cyberelephant.bank.presentation.theme.cardBorder
import com.cyberelephant.bank.presentation.theme.cardMinHeight
import com.cyberelephant.bank.presentation.theme.negativeBalance
import com.cyberelephant.bank.presentation.theme.normalMargin
import com.cyberelephant.bank.presentation.theme.positiveBalance
import com.cyberelephant.bank.presentation.theme.smallMargin
import com.cyberelephant.bank.presentation.theme.textOnPrimary
import com.cyberelephant.bank.presentation.theme.xSmallMargin
import org.koin.androidx.compose.koinViewModel

@Composable
fun BankAccountPage(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: BankAccountPageViewModel
) {

    val uiState: BankAccountPageState by viewModel.loadAccounts()
        .collectAsState(BankAccountLoading())
    val modifyBottomSheet = remember { mutableStateOf<UiBankAccount?>(null) }

    when (uiState) {
        is BankAccountLoaded ->
            Box(modifier = modifier) {
                BankAccountsList((uiState as BankAccountLoaded).bankAccounts) { accountNumber ->
                    modifyBottomSheet.value = accountNumber
                }
            }

        is BankAccountLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    }

    modifyBottomSheet.value?.let {
        ModifyBankAccountBottomSheet(
            viewModel = koinViewModel(),
            bankAccount = modifyBottomSheet.value,
            onDismiss = { modifyBottomSheet.value = null }
        )
    }

}

@Preview
@Composable
private fun BankAccountsList(
    @PreviewParameter(UiBankAccountPreviewProvider::class) bankAccounts: List<UiBankAccount>,
    onClick: ((UiBankAccount) -> Unit)? = null
) {
    BankManagementTheme {
        Box {
            LazyColumn {
                items(count = bankAccounts.count(), key = { bankAccounts[it].name }) {
                    BankAccountCard(
                        bankAccounts[it],
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun BankAccountCard(uiBankAccount: UiBankAccount, onClick: ((UiBankAccount) -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(xSmallMargin)
            .height(cardMinHeight)
            .clickable {
                onClick?.invoke(uiBankAccount)
            },
        border = cardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = normalMargin),
            horizontalArrangement = Arrangement.spacedBy(smallMargin),
        ) {
            val balance = uiBankAccount.balance
            val balanceColor = when {
                balance > 0 -> positiveBalance
                else -> negativeBalance
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
                    textAlign = TextAlign.Start,
                    color = textOnPrimary
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
                    text = uiBankAccount.accountNumber,
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = subItemModifier,
                    text = "%.2f".format(balance),
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
                (-50000..50000).random().toDouble(),
                it % 3 == 0
            )
        }.toList())

}


data class UiBankAccount(
    val accountNumber: String,
    val phoneNumber: String?,
    val name: String,
    val balance: Double,
    val isNPC: Boolean
) {
    fun toCsv(): String {
        return "$accountNumber,$name,$balance,$phoneNumber,$isNPC"
    }
}
