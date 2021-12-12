package com.rogok.weatherforecast.data.room

import androidx.room.*
import com.rogok.weatherforecast.data.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDB(currentWeatherFlow: Flow<CurrentWeatherEntity>)

    @Query("DELETE FROM current_weather")
    suspend fun deleteAllData()

    @Query("SELECT * FROM current_weather")
    suspend fun getCurrentWeatherDB(): Flow<CurrentWeatherEntity>?*/
}