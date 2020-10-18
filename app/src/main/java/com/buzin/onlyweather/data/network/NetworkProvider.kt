package com.buzin.onlyweather.data.network

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

const val API_KEY = "a81368c0da796214a41094ab9b63025e"// рабочий ключ https://github.com/search?l=Java&q=openweathermap+token&type=Code
const val UNITS = "metric"
const val BASE_URL = "https://api.openweathermap.org//data/2.5/"
const val WEATHER_ICON_URL = "https://openweathermap.org/img/w/"
const val API_LANG = "ru"

//const val BASE_URL_CITY = "https://api.openweathermap.org/data/2.5/weather?q="
//const val BASE_URL_ID = "https://api.openweathermap.org/data/2.5/weather?id="
//const val BASE_URL_LONLAT = "https://api.openweathermap.org/data/2.5/weather?lat="

class NetworkProvider @Inject constructor(val context: Context?) {

    fun isDeviceOnline(): Boolean {
        val connectivityManager = context?.getSystemService(
            Context
                .CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}