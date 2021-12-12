package com.rogok.weatherforecast.mappers

import com.rogok.weatherforecast.data.CurrentWeatherResponse
import com.rogok.weatherforecast.data.Main
import com.rogok.weatherforecast.data.Sys
import com.rogok.weatherforecast.data.Wind
import com.rogok.weatherforecast.data.room.CurrentWeatherEntity
import com.rogok.weatherforecast.models.CurrentWeather

fun CurrentWeatherResponse.toCurrentWeatherEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        idForRoom = this.id ?: 1,
        main = this.main ?: Any() as Main,
        sys = this.sys ?: Any() as Sys,
        name = this.name ?: "",
        weather = this.weather ?: emptyList(),
        wind = this.wind ?: Any() as Wind,
        requestTime = 0
    )
}

fun CurrentWeatherEntity.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        id = this.idForRoom,
        main = this.main,
        name = this.name,
        sys = this.sys,
        weather = this.weather,
        wind = this.wind,
        requestTime = this.requestTime ?: 0
    )
}

fun CurrentWeatherResponse.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        id = this.id ?: 1,
        main = this.main ?: Any() as Main,
        sys = this.sys ?: Any() as Sys,
        name = this.name ?: "",
        weather = this.weather ?: emptyList(),
        wind = this.wind ?: Any() as Wind,
        requestTime = 0
    )
}
/*

fun CurrentWeatherEntity.toCurrentWeatherError(): CurrentWeatherError {
    return CurrentWeatherError()
}*/
