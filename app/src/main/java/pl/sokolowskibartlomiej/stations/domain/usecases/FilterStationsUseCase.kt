package pl.sokolowskibartlomiej.stations.domain.usecases

import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository

class FilterStationsUseCase(private val stationsRepository: StationsRepository) {
    operator fun invoke(query: String, map: Map<String, Station>): List<Station> =
        stationsRepository.filterStations(query, map)
}