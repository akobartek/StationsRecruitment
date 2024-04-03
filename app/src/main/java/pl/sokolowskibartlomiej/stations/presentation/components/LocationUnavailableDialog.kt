package pl.sokolowskibartlomiej.stations.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WrongLocation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LocationUnavailableDialog(
    stationName: String = "",
    onDismissRequest: () -> Unit = {}
) {
    if (stationName.isNotBlank())
        AlertDialog(
            icon = { Icon(imageVector = Icons.Filled.WrongLocation, contentDescription = null) },
            title = {
                // TODO()
                Text(
                    text = "Lokalizacja niedostępna",
                    textAlign = TextAlign.Center
                )
            },
            // TODO()
            text = { Text(text = "Lokalizacja stacji $stationName jest niedostępna. Spróbuj wybrać inną stację!") },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    // TODO()
                    Text("Ok")
                }
            },
        )
}