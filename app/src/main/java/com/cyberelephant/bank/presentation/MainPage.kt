package com.cyberelephant.bank.presentation

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun MainPage(navController: NavHostController) {

    val localContext = LocalContext.current

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 80.dp)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        VerifyPermissionPart(permissionsState)

        Button(onClick = {
            navController.navigate(CyberElephantRoutes.BANK_ACCOUNTS.name)
        }) {
            Text(text = "Voir les données actuelles")
        }

        Button(onClick = {
            Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Export (CSV)")
        }

        Button(onClick = {
            Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Import (CSV)")
        }

        Button(onClick = {
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
        Text(text = "N'oubliez pas de laisser tourner l'app pour que tous les SMS reçus soient bien pris en compte :)")
    } else {
        val refusedPermissions =
            permissionsState.permissions.filter { it.status != PermissionStatus.Granted }
                .map { it.permission }
        Text(
            "Toutes les permissions n'ont pas été acceptées (manquantes : $refusedPermissions).",
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
        )
        Button(onClick = {
            permissionsState.launchMultiplePermissionRequest()
        }) {
            Text(text = "Donner les permissions")
        }
    }
}
