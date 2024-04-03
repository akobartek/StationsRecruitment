package pl.sokolowskibartlomiej.stations.domain.usecases

import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository

class GetSearchedStationsUseCase(private val stationsRepository: StationsRepository) {
    operator fun invoke() = stationsRepository.getSearchedStations()
}