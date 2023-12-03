package com.cyberelephant.bank.presentation.accounts

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Composable
fun BankAccountPage(navController: NavHostController, modifier: Modifier) {

    val currentContext = LocalContext.current

    DisposableEffect(key1 = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, effect = {

        val activity = currentContext.findActivity()
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    })

    // TODO
    val uiBankAccounts = (0..50).map {
        UiBankAccount("Pseudo $it", (-5000..5000).random())
    }.toList()
    LazyColumn {
        items(uiBankAccounts) {
            BankAccountRow(it)
        }
    }
}

@Composable
fun BankAccountRow(uiBankAccount: UiBankAccount) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = uiBankAccount.pseudo)
        Text(text = "${uiBankAccount.balance}")
    }
}

data class UiBankAccount(val pseudo: String, val balance: Int)

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}