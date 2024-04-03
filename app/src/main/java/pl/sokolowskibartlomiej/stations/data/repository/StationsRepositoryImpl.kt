package pl.sokolowskibartlomiej.stations.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import pl.sokolowskibartlomiej.stations.data.local.SearchedStationsDao
import pl.sokolowskibartlomiej.stations.data.local.entities.SearchedStationEntity
import pl.sokolowskibartlomiej.stations.data.remote.KoleoApi
import pl.sokolowskibartlomiej.stations.domain.model.Station
import pl.sokolowskibartlomiej.stations.domain.repository.StationsRepository
import java.text.Normalizer

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
}