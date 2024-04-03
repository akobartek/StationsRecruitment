package pl.sokolowskibartlomiej.stations.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.sokolowskibartlomiej.stations.data.local.entities.SearchedStationEntity

@Dao
interface SearchedStationsDao {
    @Query("SELECT id FROM searched_station ORDER BY search_timestamp DESC")
    fun getSearchedStations(): Flow<List<Int>>

    @Upsert
    fun upsertSearchedStation(entity: SearchedStationEntity)
}