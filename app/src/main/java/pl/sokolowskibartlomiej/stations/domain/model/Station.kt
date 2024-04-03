package pl.sokolowskibartlomiej.stations.domain.model

import pl.sokolowskibartlomiej.stations.data.remote.responses.StationRemote

data class Station(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val hits: Int,
    val city: String,
    val region: String,
    val country: String,
    val isGroup: Boolean
) {
    companion object {
        fun fromRemoteObject(remoteObj: StationRemote) = Station(
            id = remoteObj.id,
            name = remoteObj.name,
            latitude = remoteObj.latitude,
            longitude = remoteObj.longitude,
            hits = remoteObj.hits,
            city = remoteObj.city,
            region = remoteObj.region,
            country = remoteObj.country,
            isGroup = remoteObj.isGroup
        )
    }
}