package com.rogok.weatherforecast.data.room

import android.content.Context
import androidx.room.*
import com.rogok.weatherforecast.converters.Converters

@Database(entities = [CurrentWeatherEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
}