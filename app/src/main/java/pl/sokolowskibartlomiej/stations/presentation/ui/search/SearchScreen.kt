package pl.sokolowskibartlomiej.stations.presentation.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.sokolowskibartlomiej.stations.R
import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.presentation.components.EmptySearch
import pl.sokolowskibartlomiej.stations.presentation.components.LocationUnavailableDialog
import pl.sokolowskibartlomiej.stations.presentation.components.StationListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    isVisible: Boolean = true,
    query: String = "",
    label: String = "",
    leadingIcon: ImageVector,
    searchResults: List<Station> = listOf(),
    lastSearchedStations: List<Station> = listOf(),
    updateQuery: (String) -> Unit = {},
    saveSearchResult: (Station?) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var locationUnavailableStationName by rememberSaveable { mutableStateOf("") }
    val stations =
        if (query.isBlank() && lastSearchedStations.isNotEmpty()) lastSearchedStations
        else searchResults

    LaunchedEffect(isVisible) {
        focusManager.clearFocus(true)
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            stickyHeader {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 64.dp, bottom = 20.dp, start = 16.dp, end = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = updateQuery,
                            label = { Text(text = label) },
                            leadingIcon = {
                                Icon(imageVector = leadingIcon, contentDescription = null)
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { saveSearchResult(null) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.cd_close_searching)
                            )
                        }
                    }
                }
            }

            val numberOfResults = stations.size
            if (numberOfResults > 0)
                item {
                    Text(
                        text = stringResource(
                            id =
                            if (query.isBlank() && lastSearchedStations.isNotEmpty()) R.string.search_last_searches
                            else R.string.search_results,
                            numberOfResults
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

            items(stations, key = { it.id }) { station ->
                StationListItem(
                    station = station,
                    modifier = Modifier.clickable {
                        if (station.longitude == 0.0 || station.latitude == 0.0)
                            locationUnavailableStationName = station.name
                        else
                            saveSearchResult(station)
                    }
                )
            }

            if (query.isNotBlank() && stations.isEmpty())
                item { EmptySearch() }

            item { Spacer(modifier = Modifier.height(36.dp)) }
        }
    }

    LocationUnavailableDialog(
        stationName = locationUnavailableStationName,
        onDismissRequest = { locationUnavailableStationName = "" }
    )
}