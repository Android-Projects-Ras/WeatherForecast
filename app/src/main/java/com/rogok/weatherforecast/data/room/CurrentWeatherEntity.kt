package com.rogok.weatherforecast.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rogok.weatherforecast.converters.Converters
import com.rogok.weatherforecast.data.Main
import com.rogok.weatherforecast.data.Sys
import com.rogok.weatherforecast.data.Weather
import com.rogok.weatherforecast.data.Wind

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val idForRoom: Int,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind,
    val requestTime: Long?
)
