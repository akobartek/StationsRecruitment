package pl.sokolowskibartlomiej.stations.data.remote.responses

import com.google.gson.annotations.SerializedName

data class StationRemote(
    val id: Int,
    val name: String,
    @SerializedName("name_slug") val nameSlug: String,
    val latitude: Double,
    val longitude: Double,
    val hits: Int,
    val ibnr: Int,
    val city: String,
    val region: String,
    val country: String,
    @SerializedName("localised_name") val localisedName: String?,
    @SerializedName("is_group") val isGroup: Boolean,
    @SerializedName("has_announcements") val hasAnnouncements: Boolean,
    @SerializedName("is_nearby_station_enabled") val isNearbyStationEnabled: Boolean
)
