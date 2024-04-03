package pl.sokolowskibartlomiej.stations.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.usecases.CalculateDistanceUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.FilterStationsUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.GetSearchedStationsUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.LoadDataUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.SaveSelectedStationUseCase

data class MainScreenUiState(
    val isLoading: Boolean = true,
    val isLoadingFailed: Boolean = false,
    val stations: Map<String, Station> = mapOf(),
    val departureStation: Station? = null,
    val departureSearching: Boolean = false,
    val arrivalStation: Station? = null,
    val arrivalSearching: Boolean = false,
    val isCalculating: Boolean = false,
    val calculatedDistance: Double? = null
)

data class SearchState(
    val departureQuery: String = "",
    val arrivalQuery: String = ""
)

data class SearchResultState(
    val departureResults: List<Station> = listOf(),
    val arrivalResults: List<Station> = listOf(),
    val lastSearches: List<Station> = listOf()
)

@OptIn(FlowPreview::class)
class MainViewModel(
    private val loadDataUseCase: LoadDataUseCase,
    private val filterStationsUseCase: FilterStationsUseCase,
    private val getSearchedStationsUseCase: GetSearchedStationsUseCase,
    private val saveSelectedStationUseCase: SaveSelectedStationUseCase,
    private val calculateDistanceUseCase: CalculateDistanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _searchResultState = MutableStateFlow(SearchResultState())
    val searchResultState = _searchResultState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadDataResult = loadDataUseCase().first()
                val stations = loadDataResult.getOrThrow()
                _uiState.getAndUpdate { currentState ->
                    currentState.copy(
                        isLoading = false,
                        stations = stations
                    )
                }
            } catch (exc: Throwable) {
                _uiState.getAndUpdate { currentState ->
                    currentState.copy(isLoadingFailed = true, isLoading = false)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _searchState
                .debounce(300L)
                .combine(_uiState) { searchState, uiState ->
                    val currentQuery =
                        if (uiState.departureSearching) searchState.departureQuery
                        else searchState.arrivalQuery

                    val stations = filterStationsUseCase(currentQuery, uiState.stations)
                    _searchResultState.getAndUpdate { currentState ->
                        if (uiState.departureSearching)
                            currentState.copy(departureResults = stations)
                        else
                            currentState.copy(arrivalResults = stations)
                    }
                }
                .stateIn(this)
        }

        viewModelScope.launch(Dispatchers.IO) {
            getSearchedStationsUseCase()
                .combine(_uiState) { searchedStationsIds, state ->
                    val searchedStations = searchedStationsIds
                        .mapNotNull { id -> state.stations.values.firstOrNull { id == it.id } }
                    _searchResultState.getAndUpdate { currentState ->
                        currentState.copy(lastSearches = searchedStations)
                    }
                }
                .stateIn(this)
        }
    }

    fun swapStations() {
        _uiState.getAndUpdate { currentState ->
            currentState.copy(
                departureStation = currentState.arrivalStation,
                arrivalStation = currentState.departureStation
            )
        }
        _searchState
    }

    fun turnOffSearching() {
        _uiState.getAndUpdate { currentState ->
            currentState.copy(
                arrivalSearching = false,
                departureSearching = false
            )
        }
    }

    fun toggleArrivalSearching() {
        _uiState.getAndUpdate { currentState ->
            currentState.copy(arrivalSearching = !currentState.arrivalSearching)
        }
    }

    fun toggleDepartureSearching() {
        _uiState.getAndUpdate { currentState ->
            currentState.copy(departureSearching = !currentState.departureSearching)
        }
    }

    fun updateDepartureQuery(query: String) {
        _searchState.getAndUpdate { currentState ->
            currentState.copy(departureQuery = query)
        }
    }

    fun updateArrivalQuery(query: String) {
        _searchState.getAndUpdate { currentState ->
            currentState.copy(arrivalQuery = query)
        }
    }

    fun saveSearchResult(station: Station?) {
        _uiState.getAndUpdate { currentState ->
            station?.let {
                viewModelScope.launch(Dispatchers.IO) { saveSelectedStationUseCase(it.id) }
                currentState.copy(
                    departureStation =
                    if (currentState.departureSearching) station else currentState.departureStation,
                    departureSearching = false,
                    arrivalStation =
                    if (currentState.arrivalSearching) station else currentState.arrivalStation,
                    arrivalSearching = false,
                    calculatedDistance = null
                )
            } ?: currentState.copy(departureSearching = false, arrivalSearching = false)
        }
    }

    fun performCalculation() {
        val state = _uiState.value
        if (state.departureStation != null && state.arrivalStation != null) {
            viewModelScope.launch {
                _uiState.getAndUpdate { it.copy(isCalculating = true) }
                _uiState.getAndUpdate { currentState ->
                    val distance = calculateDistanceUseCase(
                        state.departureStation,
                        state.arrivalStation
                    )
                    currentState.copy(calculatedDistance = distance)
                }
                _uiState.getAndUpdate { it.copy(isCalculating = false) }
            }
        }
    }
}