package pl.sokolowskibartlomiej.stations.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import pl.sokolowskibartlomiej.stations.domain.repository.PreferencesRepository
import pl.sokolowskibartlomiej.stations.domain.repository.UserPreferences
import pl.sokolowskibartlomiej.stations.presentation.ui.theme.ColorTheme

val Context.dataStore by preferencesDataStore(name = PreferencesRepositoryImpl.DATA_STORE_NAME)

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesRepository {
    private object PreferencesKeys {
        val THEME = stringPreferencesKey(THEME_KEY)
        val DYNAMIC_COLORS = booleanPreferencesKey(DYNAMIC_COLORS_KEY)
    }

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exc ->
            Log.e(TAG, "Error occurred while reading preferences:", exc)
            emit(emptyPreferences())
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun updateTheme(colorTheme: ColorTheme) {
        colorTheme.setupAppCompatDelegate()
        updatePreference(colorTheme.value, PreferencesKeys.THEME)
    }

    override suspend fun updateDynamicColors(dynamicColors: Boolean) {
        updatePreference(dynamicColors, PreferencesKeys.DYNAMIC_COLORS)
    }

    private suspend fun <T> updatePreference(value: T, key: Preferences.Key<T>) {
        dataStore.edit { preferences -> preferences[key] = value }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val colorTheme = ColorTheme.fromValue(preferences[PreferencesKeys.THEME])
        val dynamicColors = preferences[PreferencesKeys.DYNAMIC_COLORS] ?: false
        return UserPreferences(colorTheme, dynamicColors)
    }

    companion object {
        const val DATA_STORE_NAME = "user_preferences"
        private const val TAG = "PreferencesRepository"
        private const val THEME_KEY = "theme"
        private const val DYNAMIC_COLORS_KEY = "dynamic_colors"
    }
}