package com.cyberelephant.bank.presentation.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.cyberelephant.bank.data.Command
import com.cyberelephant.bank.presentation.theme.normalMargin

@Composable
fun HelpPage(navController: NavController, modifier: Modifier) {

    val sealedSubclasses = Command::class.sealedSubclasses.map { it.objectInstance as Command }

    Column(modifier = modifier.padding(normalMargin)) {
        sealedSubclasses.map {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = normalMargin),
                text = it.help
            )
        }
    }


}