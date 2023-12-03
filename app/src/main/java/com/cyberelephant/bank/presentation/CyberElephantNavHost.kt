package com.cyberelephant.bank.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun CyberElephantNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CyberElephantRoutes.MAIN.name) {
        composable(CyberElephantRoutes.MAIN.name) { MainPage(navController = navController) }
        composable(CyberElephantRoutes.BANK_ACCOUNTS.name) { BankAccountPage(navController = navController) }
    }
}

enum class CyberElephantRoutes {
    MAIN,
    BANK_ACCOUNTS
}