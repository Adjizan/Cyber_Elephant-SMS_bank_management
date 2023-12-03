package com.cyberelephant.bank.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BankAccountPage(navController: NavHostController) {

    Column {
        Text(text = "Je suis sur les comptes en banque")
    }

}