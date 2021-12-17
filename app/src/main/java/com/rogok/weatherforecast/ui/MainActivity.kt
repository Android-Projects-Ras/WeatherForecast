package com.rogok.weatherforecast.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rogok.weatherforecast.APP_ACTIVITY
import com.rogok.weatherforecast.databinding.ActivityMainBinding
import com.rogok.weatherforecast.util.UIState
import com.rogok.weatherforecast.util.displayWeather
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val API_KEY = "00d9ac59b4bdc924164e509194b86c43"
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val GOOGLE_PLACES_API_KEY = "AIzaSyDyF8XK99hz7CK_5c80Z77korpnnpz2BrQ"


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<WeatherViewModel>()

    //@KoinApiExtension
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setAutocompleteSupportFragment(apiService)
        //val currentLocation = get<CurrentLocation>()
        /*val periodicWorkRequest = PeriodicWorkRequest.Builder(
            LoadWeatherData::class.java, 15, TimeUnit.MINUTES, 10, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)*/
        /**DEBUG**/
        viewModel.makeRequest()

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.weatherData.collect { state ->
                    //viewModel.weatherData.observe(this@MainActivity, Observer { state ->
                    when (state) {
                        is UIState.Success -> {
                            hideProgressBar()
                            displayWeather(state.data)
                        }
                        is UIState.Error -> {
                            hideProgressBar()
                            Toast.makeText(
                                this@MainActivity,
                                "There is an error: ${state.message}", Toast.LENGTH_LONG
                            ).show()
                        }
                        is UIState.Loading -> {
                            showProgressBar()
                        }

                    }
                }
            }
        }


        /*viewModel.iconData.observe(this, Observer {
            loadImageFromCache(it)
        })*/

        viewModel.errorLiveData.observe(this, Observer {
            binding.tvNoInternet.isVisible = it
        })

        viewModel.isLoadingLiveData.observe(this, Observer {
            binding.groupLoading.isVisible = it
        })

        viewModel.isWeatherVisibleLiveData.observe(this, Observer {
            binding.groupWeather.isVisible = it
        })


    }

    private fun hideProgressBar() {
        binding.groupLoading.isVisible = false
    }

    private fun showProgressBar() {
        binding.groupLoading.isVisible = true

    }

    override fun onStart() {
        super.onStart()
        //checkTimeToUpdate()
    }

    /*private fun checkTimeToUpdate() {  //Interactor
        viewModel.getRequestTimeAndLocationFromDB()
        viewModel.requestTimeLiveData.observe(this, Observer {
            val requestTimeDB = it
            if (isTimeToUpdate(requestTimeDB)) {
                viewModel.makeRequest()
            } else {
                //viewModel.makeRequestToDB()
            }
        })
        *//*viewModel.locationFromDBLiveData.observe(this, Observer {
            val locationFromDB = it
            if()
        })*//*
    }*/

    private fun isTimeToUpdate(requestTime: Long?): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (requestTime != null) {
            val stockTime = requestTime.plus(THIRTY_MINUTES_IN_MILL)
            currentTime > stockTime
        } else false
    }
}


/*refresh_layout.setOnRefreshListener {
            getCurrentLocationWeather()
            refresh_layout.isRefreshing = false
            APP_ACTIVITY.weather_iv.visibility = View.VISIBLE

        }*/



