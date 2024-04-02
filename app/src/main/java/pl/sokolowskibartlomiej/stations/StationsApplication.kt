package pl.sokolowskibartlomiej.stations

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import pl.sokolowskibartlomiej.stations.di.mainModule

class StationsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@StationsApplication)
            modules(
                mainModule
            )
        }
    }
}