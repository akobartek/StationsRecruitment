package pl.sokolowskibartlomiej.stations.data.remote

import pl.sokolowskibartlomiej.stations.data.remote.responses.StationRemote
import pl.sokolowskibartlomiej.stations.data.remote.responses.StationKeywordRemote
import retrofit2.http.GET

interface KoleoApi {
    @GET("stations")
    suspend fun getStations(): List<StationRemote>

    @GET("station_keywords")
    suspend fun getStationKeywords(): List<StationKeywordRemote>
}