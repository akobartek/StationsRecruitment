package pl.sokolowskibartlomiej.stations.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.sokolowskibartlomiej.stations.domain.model.Station

@Composable
fun StationListItem(
    station: Station,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
    ) {
        Text(
            text = station.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        // TODO()
        val additionalInfo =
            if (station.isGroup) "Jakikolwiek przystanek w pobliżu ${station.name}"
            else listOf(station.city, station.region, station.country)
                .filter { it.isNotBlank() }
                .joinToString(separator = ", ")
        Text(
            text = additionalInfo,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}