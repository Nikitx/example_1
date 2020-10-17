package com.buzin.onlyweather.data.database.current_db


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buzin.onlyweather.data.database.CURRENT_DB_TABLE
import com.buzin.onlyweather.data.network.response.CurrentWeather

@Entity(tableName = CURRENT_DB_TABLE)
data class CurrentWeatherModel(
    var base: String,
    val dt: Long,
    val temp: Double,
    val tempMax: Double,
    val tempMin: Double,
    val name: String,
    val visibility: Int,
    val description: String,
    val icon: String,
    val main: String,
    val windSpeed: Double,
    @PrimaryKey(autoGenerate = false)
    val cityId: Int
//    val updated: Long
) {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int //= CURRENT_WEATHER_ID
}

fun CurrentWeather.fromApiDataToModelWeather(): CurrentWeatherModel {
    return CurrentWeatherModel(
        this.base,
//        this.dt,
        System.currentTimeMillis(),
        this.main.temp,
        this.main.tempMax,
        this.main.tempMin,
        this.name,
        this.visibility,
        this.weather[0].description,
        this.weather[0].icon,
        this.weather[0].main,
        this.wind.speed,
        this.cityId
    )
}

fun createArrayFromApiWeatherList(listCurrentWeather: ArrayList<CurrentWeather>?): List<CurrentWeatherModel> {
    val listCurrentWeatherModel = arrayListOf<CurrentWeatherModel>()
    if (!listCurrentWeather.isNullOrEmpty()) {
        listCurrentWeather.forEach {
            val currentWeatherModel = CurrentWeatherModel(
                it.base,
//                it.dt,
                System.currentTimeMillis(),
                it.main.temp,
                it.main.tempMax,
                it.main.tempMin,
                it.name,
                it.visibility,
                it.weather[0].description,
                it.weather[0].icon,
                it.weather[0].main,
                it.wind.speed,
                it.cityId
            )
            listCurrentWeatherModel.add(currentWeatherModel)
        }
    }
    return listCurrentWeatherModel
}