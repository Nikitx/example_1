package com.buzin.onlyweather.data.network


import com.buzin.onlyweather.data.network.response.CurrentWeather
import com.buzin.onlyweather.data.network.response.FutureWeather
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiWeatherInterface {

    @GET("weather")
    fun getCurrentWeatherAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Deferred<CurrentWeather>

    @GET("weather")
    fun getCurrentWeatherByCityName(
        @Query("q") city: String
    ): Deferred<CurrentWeather>

    @GET("weather")
    fun getCurrentWeatherById(
        @Query("id") cityId: Int
    ): Deferred<CurrentWeather>

    @GET("group")
    fun getCurrentWeatherByIds(
        @Query("id") cityIds: List<Int>
    ): Deferred<CurrentWeather>

    @GET("forecast/daily")
    fun getFutureWeatherAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int
    ): Deferred<FutureWeather>

    @GET("forecast/daily")
    fun getFutureWeatherByCityName(
        @Query("q") city: String,
        @Query("cnt") cnt: Int
    ): Deferred<FutureWeather>


    @GET("forecast/daily")
    fun getFutureWeatherById(
        @Query("id") id: Int,
        @Query("cnt") cnt: Int
    ): Deferred<FutureWeather>


}