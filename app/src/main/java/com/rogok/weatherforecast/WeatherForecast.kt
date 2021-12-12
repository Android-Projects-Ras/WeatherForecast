package com.rogok.weatherforecast

import android.app.Application
import com.rogok.weatherforecast.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherForecast: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Koin Android logger
            androidLogger(Level.NONE)
            //inject Android context
            androidContext(this@WeatherForecast)
            // use modules
            modules(
                listOf(
                    locationModule,
                    viewModelModule,
                    roomDatabaseModule,
                    retrofitModule,
                    repositoryModule,
                    cachingModule
                )
            )
        }
    }
}