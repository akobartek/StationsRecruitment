package pl.sokolowskibartlomiej.stations.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WrongLocation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pl.sokolowskibartlomiej.stations.R

@Composable
fun LocationUnavailableDialog(
    stationName: String = "",
    onDismissRequest: () -> Unit = {}
) {
    if (stationName.isNotBlank())
        AlertDialog(
            icon = { Icon(imageVector = Icons.Filled.WrongLocation, contentDescription = null) },
            title = {
                Text(
                    text = stringResource(id = R.string.search_location_unavailable_dialog_title),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.search_location_unavailable_dialog_message,
                        stationName
                    )
                )
            },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(id = R.string.button_ok))
                }
            },
        )
}