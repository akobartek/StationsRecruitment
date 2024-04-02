package pl.sokolowskibartlomiej.stations.data.remote.responses

import com.google.gson.annotations.SerializedName

data class StationKeywordRemote(
    val id: Int,
    val keyword: String,
    @SerializedName("station_id") val stationId: Int
)
