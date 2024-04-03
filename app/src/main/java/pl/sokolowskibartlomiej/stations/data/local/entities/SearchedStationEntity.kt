package pl.sokolowskibartlomiej.stations.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searched_station")
data class SearchedStationEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "search_timestamp") val searchTimestamp: Long = 0L
)
