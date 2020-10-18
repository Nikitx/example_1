package com.buzin.onlyweather.data.network


import com.buzin.onlyweather.data.network.response.CurrentWeather
import com.buzin.onlyweather.data.network.response.FutureWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiWeatherInterface {

    @GET("weather")
    fun getCurrentWeatherByCityName(
        @Query("q") city: String
    ): Observable<CurrentWeather>

    @GET("weather")
    fun getCurrentWeatherById(
        @Query("id") cityId: Int
    ): Observable<CurrentWeather>

//    @GET("group")
//    fun getCurrentWeatherByIds(
//        @Query("id") cityIds: List<Int>
//    ): Observable<CurrentWeather>

    @GET("forecast/daily")
    fun getFutureWeatherById(
        @Query("id") id: Int,
        @Query("cnt") cnt: Int
    ): Observable<FutureWeather>

}