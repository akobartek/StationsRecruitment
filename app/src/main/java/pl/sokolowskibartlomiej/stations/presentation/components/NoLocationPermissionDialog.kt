package pl.sokolowskibartlomiej.stations.presentation.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationDisabled
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pl.sokolowskibartlomiej.stations.R

@Composable
fun NoLocationPermissionDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    if (isVisible)
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Filled.LocationDisabled,
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.permission_denied_dialog_title),
                    textAlign = TextAlign.Center
                )
            },
            text = { Text(text = stringResource(id = R.string.permission_denied_dialog_message)) },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.setData(uri)
                    context.startActivity(intent)
                }) {
                    Text(stringResource(id = R.string.settings))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(id = R.string.button_cancel))
                }
            }
        )
}