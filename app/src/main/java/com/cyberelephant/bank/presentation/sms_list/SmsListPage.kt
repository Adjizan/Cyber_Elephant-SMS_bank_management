package com.cyberelephant.bank.presentation.sms_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavHostController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.domain.use_case.UiSms
import com.cyberelephant.bank.presentation.theme.BankManagementTheme
import com.cyberelephant.bank.presentation.theme.cardMinHeight
import com.cyberelephant.bank.presentation.theme.md_theme_dark_background
import com.cyberelephant.bank.presentation.theme.normalMargin
import com.cyberelephant.bank.presentation.theme.xSmallMargin
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun SmsListPage(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: SmsListPageViewModel,
) {


    val uiState: SmsListPageState by viewModel.smsFlow.collectAsState(SmsListLoading())

    when (uiState) {
        is SmsListLoaded ->
            Box(modifier = modifier) {
                SmsList((uiState as SmsListLoaded).smsList)
            }

        is SmsListLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun SmsList(@PreviewParameter(UiSmsPreviewProvider::class) smsList: List<UiSms>) {
    BankManagementTheme {
        Box(Modifier.background(md_theme_dark_background)) {
            LazyColumn {
                items(
                    count = smsList.count(),
                    key = { "$it${smsList[it].phoneNumber}${smsList[it].message}" }) {
                    SmsRow(
                        smsList[it],
                    )
                }
            }
        }
    }
}

@Composable
fun SmsRow(uiSms: UiSms) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(xSmallMargin)
            .defaultMinSize(minHeight = cardMinHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(normalMargin)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = DateTimeFormatter
                        .ofPattern("hh:MM dd/mm/yyyy")
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault())
                        .format(uiSms.date)
                )
                Icon(
                    painter = painterResource(
                        id = if (uiSms.isIncoming) {
                            R.drawable.ic_incoming_sms
                        } else {
                            R.drawable.ic_outgoing_sms
                        }
                    ),
                    contentDescription = "Incoming message",
                    tint = (if (uiSms.isIncoming) {
                        Color.Green
                    } else {
                        Color.Red
                    })
                )
            }
            Text(text = uiSms.phoneNumber)
            Text(text = uiSms.message, maxLines = 2, overflow = TextOverflow.Ellipsis)

        }
    }

}

class UiSmsPreviewProvider : PreviewParameterProvider<List<UiSms>> {

    override val values: Sequence<List<UiSms>>
        get() = sequenceOf((0..10).map {
            UiSms(
                "0123456789",
                "Je suis un message $it",
                it % 2 == 0,
                Instant.now()
            )
        }.toList())

}
