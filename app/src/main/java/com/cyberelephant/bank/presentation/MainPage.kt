package com.cyberelephant.bank.presentation

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.cyberelephant.bank.presentation.theme.verticalMargin
import com.cyberelephant.bank.presentation.theme.xxxLargeMargin
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
fun MainPage(navController: NavHostController, modifier: Modifier) {

    val localContext = LocalContext.current

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = xxxLargeMargin
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerifyPermissionPart(permissionsState)

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                navController.navigate(CyberElephantRoutes.BANK_ACCOUNTS.name)
            }) {
            Text(text = "Voir les données actuelles")
        }

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
            }) {
            Text(text = "Export (CSV)")
        }

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
            }) {
            Text(text = "Import (CSV)")
        }

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
            }) {
            Text(text = "Help")
        }
    }

}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun VerifyPermissionPart(permissionsState: MultiplePermissionsState) {

    if (permissionsState.allPermissionsGranted) {
        Text(
            modifier = Modifier.padding(vertical = verticalMargin),
            textAlign = TextAlign.Center,
            text = "N'oubliez pas de laisser tourner l'app pour que tous les SMS reçus soient bien pris en compte :)"
        )
    } else {
        val refusedPermissions =
            permissionsState.permissions.filter { it.status != PermissionStatus.Granted }
                .map { it.permission }
        Text(
            modifier = Modifier.padding(vertical = verticalMargin),
            text = "Toutes les permissions n'ont pas été acceptées (manquantes : $refusedPermissions).",
            textAlign = TextAlign.Center,
        )
        Button(modifier = Modifier.padding(vertical = verticalMargin), onClick = {
            permissionsState.launchMultiplePermissionRequest()
        }) {
            Text(
                text = "Donner les permissions",
                modifier = Modifier.padding(vertical = verticalMargin),
            )
        }
    }
}
