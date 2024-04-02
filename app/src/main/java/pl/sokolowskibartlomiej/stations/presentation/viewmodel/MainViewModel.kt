package pl.sokolowskibartlomiej.stations.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.usecases.LoadDataUseCase

data class MainScreenUiState(
    val isLoading: Boolean = true,
    val isLoadingFailed: Boolean = false,
    val stations: List<Station> = listOf(),
    val keywordsMap: Map<String, Station?> = mapOf(),
    val departureSearching: Boolean = false,
    val arrivalSearching: Boolean = false
)

data class MainScreenInputState(
    val departureStation: Station? = null,
    val arrivalStation: Station? = null,
    val countedDistance: Float? = null
)

class MainViewModel(
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _inputState = MutableStateFlow(MainScreenInputState())
    val inputState = _inputState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadDataResult = loadDataUseCase().first()
                val (stations, map) = loadDataResult.getOrThrow()
                _uiState.getAndUpdate { currentState ->
                    currentState.copy(
                        isLoading = false,
                        stations = stations,
                        keywordsMap = map
                    )
                }
            } catch (exc: Throwable) {
                _uiState.getAndUpdate { currentState ->
                    currentState.copy(isLoadingFailed = true, isLoading = false)
                }
            }
        }
    }

    fun swapStations() {
        _inputState.getAndUpdate { currentState ->
            currentState.copy(
                departureStation = currentState.arrivalStation,
                arrivalStation = currentState.departureStation
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

    fun saveSearchResult(station: Station?) {
        _uiState.getAndUpdate { currentUiState ->
            station?.let {
                _inputState.getAndUpdate { currentInputState ->
                    if (currentUiState.departureSearching)
                        currentInputState.copy(departureStation = station)
                    else
                        currentInputState.copy(arrivalStation = station)
                }
            }
            currentUiState.copy(
                departureSearching = false,
                arrivalSearching = false
            )
        }
    }
}