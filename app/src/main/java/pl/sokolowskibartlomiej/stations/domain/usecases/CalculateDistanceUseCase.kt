package pl.sokolowskibartlomiej.stations.domain.usecases

import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository

class CalculateDistanceUseCase(private val stationsRepository: StationsRepository) {
    operator fun invoke(station1: Station, station2: Station) =
        stationsRepository.calculateDistance(station1, station2)
}