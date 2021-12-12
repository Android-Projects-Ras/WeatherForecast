package com.rogok.weatherforecast.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rogok.weatherforecast.data.Weather

class Converters {

    @TypeConverter
    fun fromStringToWeatherList(value: String?): List<Weather>? {
        val listType = object : TypeToken<List<Weather>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromWeatherListToString(list: List<Weather>?): String? {
        return Gson().toJson(list)
    }
}