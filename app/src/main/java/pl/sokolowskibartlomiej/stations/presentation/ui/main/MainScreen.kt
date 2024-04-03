package pl.sokolowskibartlomiej.stations.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import pl.sokolowskibartlomiej.stations.R
import pl.sokolowskibartlomiej.stations.presentation.components.StationField
import pl.sokolowskibartlomiej.stations.presentation.ui.search.SearchScreen
import pl.sokolowskibartlomiej.stations.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val searchResultState by viewModel.searchResultState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {
                        // TODO()
                    }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
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
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(bottom = 12.dp, start = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            // TODO() App texts
                            StationField(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Stacja początkowa",
                                stationName = uiState.departureStation?.name ?: "",
                                leadingIcon = Icons.Filled.TripOrigin,
                                onClick = viewModel::toggleDepartureSearching
                            )
                            StationField(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Stacja końcowa",
                                stationName = uiState.arrivalStation?.name ?: "",
                                leadingIcon = Icons.Filled.LocationOn,
                                onClick = viewModel::toggleArrivalSearching
                            )
                        }
                        IconButton(
                            enabled = uiState.departureStation != null || uiState.arrivalStation != null,
                            onClick = viewModel::swapStations
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SwapVert,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = ""
                            )
                        }
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(top = 16.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Text(text = "test")
                // TODO()
            }
        }
    }

    // TODO()
    SearchScreen(
        isVisible = uiState.departureSearching,
        query = searchState.departureQuery,
        label = "Stacja odjazdu",
        searchResults = searchResultState.departureResults,
        lastSearchedStations = searchResultState.lastSearches,
        updateQuery = viewModel::updateDepartureQuery,
        saveSearchResult = viewModel::saveSearchResult
    )

    SearchScreen(
        isVisible = uiState.arrivalSearching,
        query = searchState.arrivalQuery,
        label = "Stacja docelowa",
        searchResults = searchResultState.arrivalResults,
        lastSearchedStations = searchResultState.lastSearches,
        updateQuery = viewModel::updateArrivalQuery,
        saveSearchResult = viewModel::saveSearchResult
    )
}