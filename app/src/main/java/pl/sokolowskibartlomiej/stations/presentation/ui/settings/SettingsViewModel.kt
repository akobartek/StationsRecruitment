package pl.sokolowskibartlomiej.stations.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.sokolowskibartlomiej.stations.domain.repository.PreferencesRepository
import pl.sokolowskibartlomiej.stations.presentation.ui.theme.ColorTheme

class SettingsViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {

    val preferencesFlow = preferencesRepository.userPreferencesFlow

    fun updateNightMode(value: String) {
        viewModelScope.launch {
            preferencesRepository.updateTheme(ColorTheme.fromValue(value))
        }
    }

    fun updateDynamicColors(dynamicColors: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateDynamicColors(dynamicColors)
        }
    }
}