package com.cyberelephant.bank.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun BankAccountPage(navController: NavHostController, modifier: Modifier) {

    Column(modifier = modifier) {
        Text(text = "Je suis sur les comptes en banque")
    }

}