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
    val keywordsMap: Map<String, Station?> = mapOf()
)

class MainViewModel(
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

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
}