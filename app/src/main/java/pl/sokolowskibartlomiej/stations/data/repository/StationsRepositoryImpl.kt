package pl.sokolowskibartlomiej.stations.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import pl.sokolowskibartlomiej.stations.data.local.SearchedStationsDao
import pl.sokolowskibartlomiej.stations.data.local.entities.SearchedStationEntity
import pl.sokolowskibartlomiej.stations.data.remote.KoleoApi
import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository
import java.text.Normalizer
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class StationsRepositoryImpl(
    private val api: KoleoApi,
    private val dao: SearchedStationsDao
) : StationsRepository {
    override suspend fun loadData(): Flow<Result<Map<String, Station>>> = channelFlow {
        try {
            val stationsAsync = async {
                api.getStations().map { remoteObj -> Station.fromRemoteObject(remoteObj) }
            }
            val keywordsAsync = async {
                api.getStationKeywords()
            }

            val stations = stationsAsync.await()
            val keywords = keywordsAsync.await()
            val stationsMap = hashMapOf<String, Station>()
            keywords.forEach { keyword ->
                stations.firstOrNull { it.id == keyword.stationId }?.let { station ->
                    stationsMap[keyword.keyword] = station
                }
            }
            send(Result.success(stationsMap))
        } catch (exc: Exception) {
            send(Result.failure(exc))
        }
    }

    override fun filterStations(
        query: String,
        stationsMap: Map<String, Station>
    ): List<Station> {
        return stationsMap
            .filter { (keyword, _) ->
                keyword.getNormalizedString().contains(query.getNormalizedString())
            }
            .values
            .distinctBy { it.id }
            .sortedByDescending { it.hits }
            .toList()
    }

    private fun String.getNormalizedString(): String {
        val regex = Regex("[^A-Za-z0-9 ]")
        return Normalizer.normalize(this, Normalizer.Form.NFKD)
            .replace("\n", " ")
            .replace(regex, "")
            .lowercase()
    }

    override fun getSearchedStations(): Flow<List<Int>> = dao.getSearchedStations()

    override suspend fun saveSearchedStation(stationId: Int) =
        dao.upsertSearchedStation(SearchedStationEntity(stationId, System.currentTimeMillis()))

    override fun calculateDistance(station1: Station, station2: Station): Double =
        calculateDistance(
            station1.latitude,
            station1.longitude,
            station2.latitude,
            station2.longitude
        )

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371
        val lat1Radians = lat1.toRadians()
        val lat2Radians = lat2.toRadians()
        val latitudeDiff = (lat2 - lat1).toRadians()
        val longitudeDiff = (lon2 - lon1).toRadians()

        // Haversine formula
        return 2 * asin(
            sqrt(
                sin(latitudeDiff / 2).pow(2) +
                        cos(lat1Radians) * cos(lat2Radians) * sin(longitudeDiff / 2).pow(2)
            )
        ) * earthRadius
    }

    private fun Double.toRadians() = this * (Math.PI / 180)

    override fun findClosestStation(
        latitude: Double,
        longitude: Double,
        stations: List<Station>
    ): Station? {
        if (stations.isEmpty()) return null

        var closestStation = stations[0]
        var shortestDistance = calculateDistance(latitude, longitude, closestStation.latitude, closestStation.longitude)

        stations.forEach { station ->
            val distance = calculateDistance(latitude, longitude, station.latitude, station.longitude)
            if (distance < shortestDistance) {
                shortestDistance = distance
                closestStation = station
            }
        }
        return closestStation
    }
}