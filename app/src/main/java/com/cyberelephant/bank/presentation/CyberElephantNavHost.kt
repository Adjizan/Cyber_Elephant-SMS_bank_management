package com.cyberelephant.bank.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.presentation.accounts.BankAccountPage
import com.cyberelephant.bank.presentation.accounts.BankAccountPageViewModel
import com.cyberelephant.bank.presentation.accounts.ModifyBankAccountBottomSheet
import com.cyberelephant.bank.presentation.accounts.ModifyBankAccountViewModel
import com.cyberelephant.bank.presentation.help.HelpPage
import com.cyberelephant.bank.presentation.sms_list.SmsListPage
import com.cyberelephant.bank.presentation.sms_list.SmsListPageViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberElephantNavHost() {
    val navController = rememberNavController()

    val appBarActions = remember {
        mutableStateOf<AppBarActionState?>(null)
    }
    val showAddBankAccount = remember { mutableStateOf(false) }
    val currentBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()

    KoinContext {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    Timber.d(currentBackStackEntry.toString())
                    if (currentBackStackEntry?.destination?.route != mainRoute) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = stringResource(
                            id = appBarTitleByBackStackEntry(
                                currentBackStackEntry
                            )
                        )
                    )
                },
                actions = appBarActions.value?.actions ?: {}
            )
        }, content = { innerPadding: PaddingValues ->
            NavHost(
                navController = navController, startDestination = mainRoute
            ) {
                composable(mainRoute) {
                    appBarActions.value = null
                    MainPage(
                        modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                        navController = navController,
                        viewModel = koinViewModel<MainPageViewModel>()
                    )
                }
                composable(bankAccountsRoute) {

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
                composable(helpRoute) {
                    appBarActions.value = null
                    HelpPage(
                        modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                        navController = navController,
                    )
                }
            }

            if (showAddBankAccount.value) {
                ModifyBankAccountBottomSheet(viewModel = koinViewModel<ModifyBankAccountViewModel>(),
                    onDismiss = { showAddBankAccount.value = false }) {
                    showAddBankAccount.value = false
                }
            }

        })

    }

}

@StringRes
private fun appBarTitleByBackStackEntry(backStackEntry: NavBackStackEntry?): Int {
    return when (backStackEntry?.destination?.route) {
        mainRoute -> R.string.app_name
        bankAccountsRoute -> R.string.bank_account_title
        smsListRoute -> R.string.sms_title
        helpRoute -> R.string.home_help_label
        else -> R.string.app_name
    }
}

const val mainRoute: String = "main"
const val bankAccountsRoute: String = "bankAccounts"
const val smsListRoute: String = "smsList"
const val helpRoute: String = "help"

data class AppBarActionState(val actions: (@Composable RowScope.() -> Unit)? = null)
