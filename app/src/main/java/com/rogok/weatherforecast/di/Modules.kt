package com.rogok.weatherforecast.di

import androidx.room.Room
import com.rogok.weatherforecast.data.cache.CachingStrategy
import com.rogok.weatherforecast.data.cache.CachingStrategyImpl
import com.rogok.weatherforecast.data.network.OpenWeatherApiService
import com.rogok.weatherforecast.data.repository.WeatherRepository
import com.rogok.weatherforecast.data.repository.WeatherRepositoryImpl
import com.rogok.weatherforecast.data.room.WeatherDatabase
import com.rogok.weatherforecast.location.*
import com.rogok.weatherforecast.ui.BASE_URL
import com.rogok.weatherforecast.ui.WeatherViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single {
        GsonConverterFactory.create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            ).build()
    }

    single {
        get<Retrofit>().create(OpenWeatherApiService::class.java)
    }
}

val roomDatabaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            WeatherDatabase::class.java, "current_weather.db"
        )
            .build()
    }

    single {
        get<WeatherDatabase>().currentWeatherDao()
    }
}

val repositoryModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl(get(), get(), get())
    }
}

val cachingModule = module {
    single<CachingStrategy> {
        CachingStrategyImpl(get())
    }
}

val viewModelModule = module {
    viewModel {
        WeatherViewModel(get(), get())
    }
}

val locationModule = module {

    single<CurrentLocation> {
        CurrentLocationImpl(get())
    }

    single<LocationManager> {
        LocationManagerImpl(get(), get())
    }

    single<PermissionManager> {
        PermissionManagerImpl(get())
    }
}

/*@OptIn(KoinApiExtension::class)
val workerModule = module {
    *//*single <WeatherRepository>{
        WeatherRepositoryImpl(get(), get(), get())
    }*//*
    single { LoadWeatherData(get(), get(), get())}
}*/

