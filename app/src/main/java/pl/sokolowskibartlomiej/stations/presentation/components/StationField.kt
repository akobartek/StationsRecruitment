package pl.sokolowskibartlomiej.stations.presentation.components

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.sokolowskibartlomiej.stations.R

@Composable
fun StationField(
    modifier: Modifier = Modifier,
    placeholder: String,
    stationName: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Icon(
            imageVector = leadingIcon,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = null
        )
        Text(
            text = stationName.ifEmpty { placeholder },
            style = MaterialTheme.typography.titleMedium,
            color =
            if (stationName.isEmpty()) MaterialTheme.colorScheme.outline
            else MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                if (stationName.isBlank()) {
                    /** TODO Not yet implemented */
                    Toast.makeText(
                        context,
                        context.getString(R.string.not_yet_implemented),
                        Toast.LENGTH_SHORT
                    ).show()
                } else onClear()
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector =
                if (stationName.isBlank()) Icons.Filled.MyLocation else Icons.Filled.Clear,
                contentDescription = stringResource(id = R.string.cd_current_location)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationFieldPreviewEmptyName() {
    StationField(
        modifier = Modifier.fillMaxWidth(),
        placeholder = "Stacja końcowa",
        stationName = "",
        leadingIcon = Icons.Filled.LocationOn
    )
}

@Preview(showBackground = true)
@Composable
fun StationFieldPreview() {
    StationField(
        modifier = Modifier.fillMaxWidth(),
        placeholder = "Stacja końcowa",
        stationName = "Kraków Główny",
        leadingIcon = Icons.Filled.LocationOn
    )
}