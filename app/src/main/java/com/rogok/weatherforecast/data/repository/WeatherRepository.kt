package com.rogok.weatherforecast.data.repository

import android.util.Log
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import com.rogok.weatherforecast.data.room.CurrentWeatherDao
import com.rogok.weatherforecast.location.LocationManager
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    //fun saveDataToDB(cityName: String)
    //suspend fun getDataFromDB(): Flow<CurrentWeather>
    //suspend fun getCurrentLocationWeather()
    //suspend fun eraseDataFromDB()
    suspend fun getWeatherFromAPI(): Flow<CurrentWeatherResponse>?
}

class WeatherRepositoryImpl(
    private val api: OpenWeatherApiService,
    private val currentWeatherDao: CurrentWeatherDao,
    private val locationManager: LocationManager
) : WeatherRepository {

    //This fun invokes multiple times running
    /*override suspend fun getCurrentLocationWeather() {
        //eraseDataFromDB()
        //try {
        //lateinit var openWeatherLocation: OpenWeatherLocation
        locationManager.startLocation {
            val cityName = locationManager.getCityName(it)
            if (cityName != null) {
                Log.d("Location", cityName)
            }
            if (cityName != null) {
                saveDataToDB(cityName)
            }
        }


        *//*if (cityName != null) {
            saveDataToDB(cityName)
        }*//*

        //} catch (e: Exception) {
        *//*Log.d("Rogok_WeatherRepository", "getCurrentLocationWeather:${e.message} ")
        val curLocation = locationManager.getCurrentLoc()
        val cityName = locationManager.getCityName(curLocation)
        cityName?.let {
            saveDataToDB(it)
        }*//*
        // }
    }*/

    /*override suspend fun getDataFromDB(): Flow<CurrentWeather> {
        return flow {
            currentWeatherDao.getCurrentWeatherDB()?.map {
                emit(it.toCurrentWeather())
            }
                ?.catch { e->
                    Log.d("WeatherPero", "getDataFromDB: $e")
                    emit(CurrentWeather(
                        id = 0,
                        main = Main(
                            humidity = 0,
                    temp = 0.0,
                    tempMax = 0.0,
                    tempMin = 0.0
                    ),
                    sys = Sys(""),
                    name = "",
                    weather = emptyList(),
                    wind = Wind(speed = 0.0),
                    requestTime = 0
                    ))
                }
        }
    }*/

    /**
    Wiwi🍻, [07.12.2021 19:50]
    [В ответ на Андрей Рожков]
    А если апи ошибку вернет то что делать будешь?

    Wiwi🍻, [07.12.2021 19:51]
    По хорошему надо сначала апи дернуть. А там в зависимости от результата решать что с этим делать

    Wiwi🍻, [07.12.2021 19:52]
    Ну и апи дергает один метод а в базу пишет другой чтоб переиспользовать можно было**/
//This fun invokes multiple times running
    /*override fun saveDataToDB(cityName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val requestTime = System.currentTimeMillis()
            val weather = api.getCurrentWeather(cityName)
            Log.d("Rogok_WeatherRepository", "saveDataToDB: $weather")
            currentWeatherDao.upsertDB(
                flow {
                    weather.map {
                        emit(
                            it.toCurrentWeatherEntity()
                                .copy(requestTime = requestTime)
                        )
                    }
                }//toCurrentWeatherEntity().copy(requestTime = requestTime)
            )
        }
    }*/

    /*override suspend fun eraseDataFromDB() {
        currentWeatherDao.deleteAllData()
    }*/

    override suspend fun getWeatherFromAPI(): Flow<CurrentWeatherResponse>? {
        val openWeatherLocation = locationManager.startLocation {
        val cityName = locationManager.getCityName(it)

        }
        val weather = cityName?.let { api.getCurrentWeather(it) }
        if (cityName != null) {
            Log.d("WRepo,getWeatherFromAPI", cityName)
            Log.d("WRepo,getWeatherFromAPI", "$weather")
        }
        return weather
    }
}