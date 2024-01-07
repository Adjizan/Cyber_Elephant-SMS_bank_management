package com.cyberelephant.bank.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.presentation.accounts.BankAccountPage
import com.cyberelephant.bank.presentation.accounts.BankAccountPageViewModel
import com.cyberelephant.bank.presentation.accounts.ModifyBankAccountBottomSheet
import com.cyberelephant.bank.presentation.accounts.ModifyBankAccountViewModel
import com.cyberelephant.bank.presentation.sms_list.SmsListPage
import com.cyberelephant.bank.presentation.sms_list.SmsListPageViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberElephantNavHost() {
    val navController = rememberNavController()

    val appBarTitle = remember { mutableIntStateOf(R.string.app_name) }
    val appBarActions = remember {
        mutableStateOf<AppBarActionState?>(null)
    }
    val showAddBankAccount = remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ), title = {
                Text(text = stringResource(id = appBarTitle.intValue))
            }, actions = appBarActions.value?.actions ?: {}
        )
    }, content = { innerPadding: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = mainRoute
        ) {
            composable(mainRoute) {
                appBarTitle.intValue = (R.string.app_name)
                appBarActions.value = null
                MainPage(
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    navController = navController,
                    viewModel = koinViewModel<MainPageViewModel>()
                )
            }
            composable(bankAccountsRoute) {
                appBarTitle.intValue = R.string.bank_account_title
                appBarActions.value = AppBarActionState(actions = {
                    IconButton(onClick = {
                        showAddBankAccount.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.bank_account_add)
                        )
                    }
                })
                BankAccountPage(
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    navController = navController,
                    viewModel = koinViewModel<BankAccountPageViewModel>()
                )
            }
            composable(smsListRoute) {

                val smsListPageViewModel = koinViewModel<SmsListPageViewModel>()
                appBarTitle.intValue = R.string.sms_title
                appBarActions.value = AppBarActionState(actions = {
                    IconButton(onClick = {
                        smsListPageViewModel.filterIncomingSms()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_incoming_sms),
                            contentDescription = stringResource(R.string.sms_list_incoming_sms_filter_accessibility),
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = {
                        smsListPageViewModel.filterOutgoingSms()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outgoing_sms),
                            contentDescription = stringResource(R.string.sms_list_outgoing_sms_filter_accessibility),
                            tint = Color.Green
                        )
                    }
                    IconButton(onClick = {
                        smsListPageViewModel.allSms()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_all_sms),
                            contentDescription = stringResource(R.string.sms_list_all_sms_filter_accessibility),
                            tint = Color.Black
                        )
                    }
                })
                SmsListPage(
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    navController = navController,
                    viewModel = smsListPageViewModel,
                )
            }
        }

        if (showAddBankAccount.value) {
            ModifyBankAccountBottomSheet(
                viewModel = koinViewModel<ModifyBankAccountViewModel>(),
                onDismiss = { showAddBankAccount.value = false }
            ) { showAddBankAccount.value = false }
        }

    })
}

const val mainRoute: String = "main"
const val bankAccountsRoute: String = "bankAccounts"
const val smsListRoute: String = "smsList"

data class AppBarActionState(val actions: (@Composable RowScope.() -> Unit)? = null)