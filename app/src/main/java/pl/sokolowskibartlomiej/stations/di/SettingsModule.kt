package pl.sokolowskibartlomiej.stations.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.sokolowskibartlomiej.stations.data.repository.PreferencesRepositoryImpl
import pl.sokolowskibartlomiej.stations.data.repository.dataStore
import pl.sokolowskibartlomiej.stations.domain.repository.PreferencesRepository
import pl.sokolowskibartlomiej.stations.presentation.ui.settings.SettingsViewModel

val settingsModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(androidContext().dataStore) }

    viewModel { SettingsViewModel(get()) }
}