package pl.sokolowskibartlomiej.stations.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.sokolowskibartlomiej.stations.data.repository.StationsRepositoryImpl
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository
import pl.sokolowskibartlomiej.stations.domain.usecases.FilterStationsUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.GetSearchedStationsUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.LoadDataUseCase
import pl.sokolowskibartlomiej.stations.domain.usecases.SaveSelectedStationUseCase
import pl.sokolowskibartlomiej.stations.presentation.viewmodel.MainViewModel

val mainModule = module {
    single<StationsRepository> { StationsRepositoryImpl(get(), get()) }
    single { LoadDataUseCase(get()) }
    single { FilterStationsUseCase(get()) }
    single { GetSearchedStationsUseCase(get()) }
    single { SaveSelectedStationUseCase(get()) }

    viewModel { MainViewModel(get(), get(), get(), get()) }
}