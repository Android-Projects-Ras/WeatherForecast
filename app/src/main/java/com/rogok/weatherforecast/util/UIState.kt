package com.rogok.weatherforecast.util

sealed class UIState<T>(
    val data: T? = null,
    val message: String? = null,
    /*var isLoading: Boolean = false*/
) {

    class Success<T>(data: T?) : UIState<T>(data)
    class Error<T>(data: T? = null, message: String) : UIState<T>(data, message)
    class Loading<T>(/*isLoading: Boolean = true*/) : UIState<T>(/*isLoading = true*/)
}
