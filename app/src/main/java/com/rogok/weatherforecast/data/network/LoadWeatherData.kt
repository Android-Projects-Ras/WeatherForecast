/*
package com.rogok.weatherforecast.data.network

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rogok.weatherforecast.data.repository.WeatherRepository
import com.rogok.weatherforecast.data.repository.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class LoadWeatherData(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val repository: WeatherRepository by inject()


    private val errorHandler = CoroutineExceptionHandler { context, throwable ->
    }


    //Update room database
    override suspend fun doWork(): Result {
        //val repository: WeatherRepository by inject()

        return try {
            Log.d("Rogok_Start", "doWork: 1")

            repository.getCurrentLocationWeather()

            Log.d("Rogok_Finish", "doWork: 2")

            Result.success()
        } catch (e: Exception) {
            Log.d("Rogok_Start", "doWork:${e.message}")
            Result.failure()
        }
    }
}
*/
