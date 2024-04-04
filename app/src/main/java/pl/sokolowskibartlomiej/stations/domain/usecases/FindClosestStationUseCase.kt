package pl.sokolowskibartlomiej.stations.domain.usecases

import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository

class FindClosestStationUseCase(private val stationsRepository: StationsRepository) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        stations: List<Station>
    ): Station? =
        stationsRepository.findClosestStation(latitude, longitude, stations)
}