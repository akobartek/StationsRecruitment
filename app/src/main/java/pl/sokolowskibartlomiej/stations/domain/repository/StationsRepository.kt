package pl.sokolowskibartlomiej.stations.domain.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import pl.sokolowskibartlomiej.stations.data.remote.KoleoApi
import pl.sokolowskibartlomiej.stations.domain.model.Station

class StationsRepository(private val api: KoleoApi) {
    suspend fun loadData(): Flow<Result<Pair<List<Station>, Map<String, Station?>>>> = channelFlow {
        try {
            val stationsAsync = async {
                api.getStations().map { remoteObj -> Station.fromRemoteObject(remoteObj) }
            }
            val keywordsAsync = async {
                api.getStationKeywords()
            }

            val stations = stationsAsync.await()
            val keywordsMap = keywordsAsync.await().associate { keywordObj ->
                keywordObj.keyword to stations.firstOrNull { it.id == keywordObj.stationId }
            }
            send(Result.success(Pair(stations, keywordsMap)))
        } catch (exc: Exception) {
            send(Result.failure(exc))
        }
    }
}