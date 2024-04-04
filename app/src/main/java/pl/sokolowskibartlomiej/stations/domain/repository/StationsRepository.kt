package pl.sokolowskibartlomiej.stations.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.sokolowskibartlomiej.stations.domain.model.Station

interface StationsRepository {
    suspend fun loadData(): Flow<Result<Map<String, Station>>>

    fun filterStations(query: String, stationsMap: Map<String, Station>): List<Station>

    fun getSearchedStations(): Flow<List<Int>>

    suspend fun saveSearchedStation(stationId: Int)

    fun calculateDistance(station1: Station, station2: Station): Double

    fun findClosestStation(latitude: Double, longitude: Double, stations: List<Station>): Station?
}