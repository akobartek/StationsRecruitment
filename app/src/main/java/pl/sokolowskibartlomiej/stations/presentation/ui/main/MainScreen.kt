package pl.sokolowskibartlomiej.stations.presentation.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import pl.sokolowskibartlomiej.stations.R
import pl.sokolowskibartlomiej.stations.presentation.components.DistanceCalculationDrawing
import pl.sokolowskibartlomiej.stations.presentation.components.LoadingDialog
import pl.sokolowskibartlomiej.stations.presentation.components.NoInternetDialog
import pl.sokolowskibartlomiej.stations.presentation.components.StationField
import pl.sokolowskibartlomiej.stations.presentation.ui.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    openSettings: () -> Unit,
    finish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val searchResultState by viewModel.searchResultState.collectAsStateWithLifecycle()

    BackHandler {
        if (!uiState.arrivalSearching || !uiState.departureSearching)
            viewModel.saveSearchResult()
        else
            finish()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontStyle = FontStyle.Italic
                    )
                },
                navigationIcon = {
                    IconButton(onClick = openSettings) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(MaterialTheme.colorScheme.surfaceVariant)
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
                            StationField(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(id = R.string.departure_station),
                                stationName = uiState.departureStation?.name ?: "",
                                leadingIcon = Icons.Filled.TripOrigin,
                                onClick = viewModel::toggleDepartureSearching
                            )
                            StationField(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = stringResource(id = R.string.arrival_station),
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
                                contentDescription = stringResource(id = R.string.cd_swap_stations)
                            )
                        }
                    }
                    Button(
                        onClick = viewModel::performCalculation,
                        enabled =
                        uiState.departureStation != null && uiState.arrivalStation != null && !uiState.isCalculating,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(top = 16.dp, end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.cd_search_for_distance)
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                if (uiState.isCalculating) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 40.dp))
                }
                AnimatedVisibility(
                    visible = uiState.calculatedDistance != null,
                    enter = slideInVertically(initialOffsetY = { it * 2 }),
                    exit = slideOutVertically(targetOffsetY = { it * 2 })
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DistanceCalculationDrawing()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(
                                id = R.string.distance_result_message,
                                uiState.calculatedDistance ?: 0.0
                            ),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    SearchScreen(
        isVisible = uiState.departureSearching,
        query = searchState.departureQuery,
        label = stringResource(id = R.string.departure_station),
        leadingIcon = Icons.Filled.TripOrigin,
        searchResults = searchResultState.departureResults,
        lastSearchedStations = searchResultState.lastSearches,
        updateQuery = viewModel::updateDepartureQuery,
        saveSearchResult = viewModel::saveSearchResult
    )

    SearchScreen(
        isVisible = uiState.arrivalSearching,
        query = searchState.arrivalQuery,
        label = stringResource(id = R.string.arrival_station),
        leadingIcon = Icons.Filled.LocationOn,
        searchResults = searchResultState.arrivalResults,
        lastSearchedStations = searchResultState.lastSearches,
        updateQuery = viewModel::updateArrivalQuery,
        saveSearchResult = viewModel::saveSearchResult
    )

    LoadingDialog(isVisible = uiState.isLoading)

    NoInternetDialog(
        isVisible = uiState.isLoadingFailed,
        onReconnect = {
            viewModel.toggleLoadingErrorDialog()
            viewModel.loadData()
        },
        onDismiss = viewModel::toggleLoadingErrorDialog
    )
}