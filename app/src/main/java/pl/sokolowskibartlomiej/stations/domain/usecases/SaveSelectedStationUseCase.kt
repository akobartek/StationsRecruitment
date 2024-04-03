package pl.sokolowskibartlomiej.stations.domain.usecases

import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository

class SaveSelectedStationUseCase(private val stationsRepository: StationsRepository) {
    suspend operator fun invoke(stationId: Int) = stationsRepository.saveSearchedStation(stationId)
}