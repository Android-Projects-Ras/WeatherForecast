package com.rogok.weatherforecast.ui

import android.location.Location
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogok.weatherforecast.data.cache.CachingStrategy
import com.rogok.weatherforecast.data.repository.WeatherRepository
import com.rogok.weatherforecast.mappers.toCurrentWeather
import com.rogok.weatherforecast.models.CurrentWeather
import com.rogok.weatherforecast.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

const val THIRTY_MINUTES_IN_MILL = 1000 * 60 * 30
const val TEN_SECONDS_IN_MILL = 1000 * 10

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val cachingStrategy: CachingStrategy
) : ViewModel() {

   /* private var _weatherData = MutableLiveData<UIState<CurrentWeather>>()
    val weatherData: LiveData<UIState<CurrentWeather>> = _weatherData*/
    private var _weatherData = MutableStateFlow<UIState<CurrentWeather>>(UIState.Loading())
    val weatherData = _weatherData.asStateFlow()

    private var _iconData = MutableLiveData<Uri>()
    val iconData: LiveData<Uri> = _iconData

    private var _errorLiveData = MutableLiveData<Boolean>()
    val errorLiveData: LiveData<Boolean> = _errorLiveData

    private var _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private var _requestTimeLiveData = MutableLiveData<Long?>()
    val requestTimeLiveData: LiveData<Long?> = _requestTimeLiveData

    private var _isWeatherVisibleLiveData = MutableLiveData<Boolean>()
    val isWeatherVisibleLiveData: LiveData<Boolean> = _isWeatherVisibleLiveData

    private var _locationFromDBLiveData = MutableLiveData<String>()
    val locationFromDBLiveData: LiveData<String> = _locationFromDBLiveData

    var _locationLiveData = MutableLiveData<Location>()
    val locationLiveData: LiveData<Location> = _locationLiveData

    /*init {
        makeRequest()
        makeRequestToDB()
    }*/

     /*val weather: Flow<CurrentWeather> = flow {
         val weatherFromAPI = repository
             .getWeatherFromAPI()
             .toCurrentWeather()
         emit(weatherFromAPI)
     }.catch {

     }*/


    fun makeRequest() {

        viewModelScope.launch(Dispatchers.IO) {
            _weatherData.value = UIState.Loading()
            _weatherData.value = handleWeatherResponse()

        }
    }

    private suspend fun handleWeatherResponse(): UIState<CurrentWeather> {
        return try {
            UIState.Success(
                repository
                    .getWeatherFromAPI()
                    .toCurrentWeather()
            )
        } catch (e: Exception) {
            UIState.Error(
                data = null,
                message = e.message.toString()
            )
        }

    }

    /*fun makeRequestToDB() {
        viewModelScope.launch(Dispatchers.IO) {

            val currentWeather = repository.getDataFromDB()



            if (currentWeather != null) {
                repository.getWeatherFromAPI()
                    ?.toCurrentWeather()
                    .also { _weatherData.value = it }

            }
        }
    }*/

    private suspend fun getDataFromDB(currentWeather: CurrentWeather) {
        _isLoadingLiveData.postValue(true)
        _errorLiveData.postValue(false)
        _isWeatherVisibleLiveData.postValue(false)
        val iconCode = currentWeather.weather[0].icon        //01d
        val iconName = cachingStrategy.getIconName(iconCode) //01d@2x.png

        if (cachingStrategy.isImageExists(iconName)) {
            cachingStrategy.loadImageFromCache(iconName.toUri())
        } else {
            val imageBitmap = cachingStrategy.loadBitmapAsync(iconCode)
            val savedIcon = cachingStrategy.saveBitmap(imageBitmap, iconName)
            _iconData.postValue(savedIcon)

        }
        //to Main Activity
        /*viewModelScope.launch(Dispatchers.Main) {
            currentWeather.let {
                _weatherData.value = it
            }
        }*/
        _isLoadingLiveData.postValue(false)
        _isWeatherVisibleLiveData.postValue(true)
    }

    /*fun getRequestTimeAndLocationFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentWeather = repository.getDataFromDB()

            val requestTime = currentWeather.collect{
                it.requestTime
            }


            *//*currentWeather?.name.also {
                _locationFromDBLiveData.postValue(it)
            }

            _requestTimeLiveData.postValue(requestTime)*//*
        }
    }*/
}