package pl.sokolowskibartlomiej.stations.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.sokolowskibartlomiej.stations.data.local.entities.SearchedStationEntity

@Database(
    entities = [SearchedStationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StationsDatabase : RoomDatabase() {
    abstract fun searchedStationsDao(): SearchedStationsDao
}