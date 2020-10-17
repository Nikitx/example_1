package com.buzin.onlyweather.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

//const val API_KEY = "8a832c01210943d21ff1495629d9d6c4"// кривой ключ
const val API_KEY = "9559b7571ec00e4dd0b80e9337083adc"
const val UNITS = "metric"
const val BASE_URL = "https://api.openweathermap.org//data/2.5/"
const val WEATHER_ICON_URL = "https://openweathermap.org/img/w/"
const val API_LANG = "ru"

//const val BASE_URL_CITY = "https://api.openweathermap.org/data/2.5/weather?q="
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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BaseUrlChangingInterceptor : Interceptor {

    private var httpUrl: HttpUrl? = HttpUrl.parse(BASE_URL)


    fun setInterceptor(url: String) {
        httpUrl = HttpUrl.parse(url)!!
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .newBuilder()
            .url(httpUrl)
            .build()
            .url()
            .newBuilder()
            .addQueryParameter("appid", API_KEY)
            .addQueryParameter("units", UNITS)
            .build()
        Log.d("aaaa", "url ${url.toString()} ")

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        // Log.d("aaaa", "${Gson().toJson(request)} ")

        return chain.proceed(request)
    }

    companion object {
        private var sInterceptor: BaseUrlChangingInterceptor? = null

        fun get(): BaseUrlChangingInterceptor {
            if (sInterceptor == null) {
                sInterceptor = BaseUrlChangingInterceptor()
            }
            return sInterceptor as BaseUrlChangingInterceptor
        }
    }
}