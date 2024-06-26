package pl.sokolowskibartlomiej.stations.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import pl.sokolowskibartlomiej.stations.R

@Composable
fun MultiSelectDialog(
    isVisible: Boolean,
    imageVector: ImageVector,
    titleId: Int,
    currentValue: String,
    values: Array<String>,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        val (selectedValue, onValueSelected) = remember { mutableStateOf(currentValue) }

        AlertDialog(
            icon = { Icon(imageVector = imageVector, contentDescription = null) },
            title = { Text(text = stringResource(id = titleId)) },
            text = {
                Column(Modifier.selectableGroup()) {
                    values.forEach { value ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (value == selectedValue),
                                    onClick = { onValueSelected(value) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (value == selectedValue),
                                onClick = null
                            )
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        onSave(selectedValue)
                    }
                ) {
                    Text(stringResource(id = R.string.button_save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(id = R.string.button_cancel))
                }
            }
        )
    }
}