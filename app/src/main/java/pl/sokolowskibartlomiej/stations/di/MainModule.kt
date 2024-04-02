package pl.sokolowskibartlomiej.stations.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository
import pl.sokolowskibartlomiej.stations.domain.usecases.LoadDataUseCase
import pl.sokolowskibartlomiej.stations.presentation.viewmodel.MainViewModel

val mainModule = module {
    single { StationsRepository(get()) }
    single { LoadDataUseCase(get()) }

    viewModel { MainViewModel(get()) }
}