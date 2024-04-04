package pl.sokolowskibartlomiej.stations.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pl.sokolowskibartlomiej.stations.R

@Composable
fun NoInternetDialog(
    isVisible: Boolean,
    onReconnect: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible)
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Default.SignalCellularConnectedNoInternet0Bar,
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.no_internet_dialog_title),
                    textAlign = TextAlign.Center
                )
            },
            text = { Text(text = stringResource(id = R.string.no_internet_dialog_message)) },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = onReconnect) {
                    Text(stringResource(id = R.string.button_try_again))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(id = R.string.button_cancel))
                }
            }
        )
}