package pl.sokolowskibartlomiej.stations.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.sokolowskibartlomiej.stations.presentation.ui.theme.ColorTheme

data class UserPreferences(
    val colorTheme: ColorTheme = ColorTheme.SYSTEM,
    val dynamicColors: Boolean = false
)

interface PreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateTheme(colorTheme: ColorTheme)

    suspend fun updateDynamicColors(dynamicColors: Boolean)
}