package pl.sokolowskibartlomiej.stations

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import pl.sokolowskibartlomiej.stations.di.databaseModule
import pl.sokolowskibartlomiej.stations.di.mainModule
import pl.sokolowskibartlomiej.stations.di.networkModule

class StationsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@StationsApplication)
            modules(
                networkModule,
                databaseModule,
                mainModule,
            )
        }
    }
}