package com.cyberelephant.bank.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.presentation.accounts.BankAccountPage
import com.cyberelephant.bank.presentation.accounts.BankAccountPageViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberElephantNavHost() {
    val navController = rememberNavController()

    val appBarTitle = remember { mutableIntStateOf(R.string.app_name) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text(text = stringResource(id = appBarTitle.intValue))
        })
    }, content = { innerPadding: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = CyberElephantRoutes.MAIN.name
        ) {
            composable(CyberElephantRoutes.MAIN.name) {
                appBarTitle.intValue = (R.string.app_name)
                MainPage(
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    navController = navController
                )
            }
            composable(CyberElephantRoutes.BANK_ACCOUNTS.name) {
                appBarTitle.intValue = R.string.bank_account_title
                BankAccountPage(
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    navController = navController,
                    viewModel = koinViewModel<BankAccountPageViewModel>()
                )
            }
        }

    })
}

enum class CyberElephantRoutes {
    MAIN,
    BANK_ACCOUNTS
}