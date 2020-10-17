package com.buzin.onlyweather.data.database.future_db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buzin.onlyweather.data.database.FORECAST_DB_TABLE
import com.buzin.onlyweather.data.network.response.FutureCityData
import com.buzin.onlyweather.data.network.response.FutureWeatherListObject
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
@Entity(tableName = FORECAST_DB_TABLE)
data class FutureWeatherModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val clouds: Int,
    val deg: Int,
    val dt: Long,
    val humidity: Int,
    val pressure: Double,
    val snow: Double,
    val speed: Double,
    val max: Double,
    val min: Double,
    val description: String,
    val icon: String,
    val main: String,
    val cityId: Int
) : Parcelable

fun createArrayFromApiWeatherList(
    listFutureWeather: ArrayList<FutureWeatherListObject>?,
    futureCityData: FutureCityData
): List<FutureWeatherModel> {
    val listFutureWeatherModel = arrayListOf<FutureWeatherModel>()
    if (!listFutureWeather.isNullOrEmpty()) {
        listFutureWeather.forEach {
            val futureWeatherListObjectModel = FutureWeatherModel(
                0,
                it.clouds,
                it.deg,
                it.dt,
                it.humidity,
                it.pressure,
                it.snow,
                it.speed,
                it.temp.max,
                it.temp.min,
                it.weather[0].description,
                it.weather[0].icon,
                it.weather[0].main,
                futureCityData.cityId
            )
            listFutureWeatherModel.add(futureWeatherListObjectModel)
        }
    }
    return listFutureWeatherModel
}
