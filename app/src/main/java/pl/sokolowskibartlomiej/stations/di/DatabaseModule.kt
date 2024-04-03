package pl.sokolowskibartlomiej.stations.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import pl.sokolowskibartlomiej.stations.data.local.SearchedStationsDao
import pl.sokolowskibartlomiej.stations.data.local.StationsDatabase

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StationsDatabase::class.java,
            "stations.db"
        ).build()
    }
    single<SearchedStationsDao> {
        val db = get<StationsDatabase>()
        db.searchedStationsDao()
    }
}