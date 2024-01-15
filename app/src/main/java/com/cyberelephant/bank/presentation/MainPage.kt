package com.cyberelephant.bank.presentation

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.cyberelephant.bank.R
import com.cyberelephant.bank.presentation.theme.cardBorder
import com.cyberelephant.bank.presentation.theme.roundedCornerRadius
import com.cyberelephant.bank.presentation.theme.smallMargin
import com.cyberelephant.bank.presentation.theme.verticalMargin
import com.cyberelephant.bank.presentation.theme.xxxLargeMargin
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun MainPage(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: MainPageViewModel
) {

    val localContext = LocalContext.current

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )
    )

    val coroutineScope = rememberCoroutineScope()

    val chooseFileToImport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { documentUri ->
            if (documentUri == null) {
                Toast.makeText(
                    localContext,
                    localContext.getString(R.string.import_bank_accounts_no_file_selected),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@rememberLauncherForActivityResult
            }

            val uri: Uri = documentUri

            val contentResolver = localContext.contentResolver
            coroutineScope.launch {
                viewModel.importBankCSVData(contentResolver.openInputStream(uri)).collect {
                    when (it) {
                        true -> {
                            Toast.makeText(
                                localContext,
                                localContext.getString(R.string.bank_account_import_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        false -> {
                            Toast.makeText(
                                localContext,
                                localContext.getString(R.string.bank_account_import_failure),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                localContext,
                                localContext.getString(R.string.bank_account_import_in_progress),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

            }
        }

    val chooseFileToExport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("text/comma-separated-values")) { uri ->
            uri?.let {
                coroutineScope.launch {
                    viewModel.exportBankCSVData(localContext.contentResolver, uri)
                        .collect { success ->
                            when (success) {
                                true -> Toast.makeText(
                                    localContext,
                                    localContext.getString(R.string.bank_account_successful_export),
                                    Toast.LENGTH_SHORT
                                ).show()

                                false -> Toast.makeText(
                                    localContext,
                                    localContext.getString(R.string.bank_account_failed_export),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = xxxLargeMargin
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerifyPermissionPart(permissionsState)

        Button(modifier = Modifier
            .padding(smallMargin)
            .border(
                cardBorder,
                shape = RoundedCornerShape(roundedCornerRadius)
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                navController.navigate(bankAccountsRoute)
            }) {
            Text(text = stringResource(R.string.home_current_label))
        }

        Button(modifier = Modifier
            .padding(smallMargin)
            .border(
                cardBorder,
                shape = RoundedCornerShape(roundedCornerRadius)
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                chooseFileToImport.launch(arrayOf("text/comma-separated-values"))
            }) {
            Text(text = stringResource(R.string.home_import_label))
        }

        Button(
            modifier = Modifier
                .padding(smallMargin)
                .border(
                    cardBorder,
                    shape = RoundedCornerShape(roundedCornerRadius)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                chooseFileToExport.launch(
                    "cyber-elephant_bank_${
                        DateTimeFormatter.ofPattern("yyyyMMdd_hh:mm:ss")
                            .withLocale(Locale.getDefault())
                            .withZone(ZoneId.systemDefault())
                            .format(Instant.now())
                    }.csv"
                )
            }) {
            Text(text = stringResource(R.string.home_export_label))
        }

        Button(modifier = Modifier
            .padding(smallMargin)
            .border(
                cardBorder,
                shape = RoundedCornerShape(roundedCornerRadius)
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                navController.navigate(smsListRoute)
            }) {
            Text(text = stringResource(R.string.home_sms_list_label))
        }

        Button(modifier = Modifier
            .padding(smallMargin)
            .border(
                cardBorder,
                shape = RoundedCornerShape(roundedCornerRadius)
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                navController.navigate(helpRoute)
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
