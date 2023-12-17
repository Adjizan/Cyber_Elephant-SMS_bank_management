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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.cyberelephant.bank.R
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
            Text(text = stringResource(R.string.home_current_label))
        }

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
            }) {
            Text(text = stringResource(R.string.home_export_label))
        }

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
            }) {
            Text(text = stringResource(R.string.home_import_label))
        }

        Button(
            modifier = Modifier.padding(vertical = verticalMargin), onClick = {
                Toast.makeText(localContext, "TODO", Toast.LENGTH_SHORT).show()
            }) {
            Text(text = stringResource(R.string.home_help_label))
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
            text = stringResource(R.string.home_permission_ok_label)
        )
    } else {
        val refusedPermissions =
            permissionsState.permissions.filter { it.status != PermissionStatus.Granted }
                .map { it.permission }
        Text(
            modifier = Modifier.padding(vertical = verticalMargin),
            text = stringResource(R.string.home_permission_denied_label, refusedPermissions),
            textAlign = TextAlign.Center,
        )
        Button(modifier = Modifier.padding(vertical = verticalMargin), onClick = {
            permissionsState.launchMultiplePermissionRequest()
        }) {
            Text(
                text = stringResource(R.string.home_give_permission_label),
                modifier = Modifier.padding(vertical = verticalMargin),
            )
        }
    }
}
