package com.buzin.onlyweather.data.database.future_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buzin.onlyweather.data.database.FIELD_CITY_ID
import com.buzin.onlyweather.data.database.FORECAST_DB_TABLE
import com.buzin.onlyweather.data.database.FIELD_WEATHER_DATE


@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFutureListAll(list: List<FutureWeatherModel>)

    @Query("SELECT * FROM $FORECAST_DB_TABLE")
    suspend fun getFutureList(): List<FutureWeatherModel>

    @Query("SELECT * FROM $FORECAST_DB_TABLE WHERE $FIELD_CITY_ID = :cityId")
    fun getAllDaysForecastList(cityId: Int): LiveData<MutableList<FutureWeatherModel>>

    @Query("SELECT * FROM $FORECAST_DB_TABLE WHERE $FIELD_CITY_ID = :cityId")
    suspend fun getFutureListByCityId(cityId: Int): List<FutureWeatherModel>

    @Query("SELECT * FROM $FORECAST_DB_TABLE WHERE $FIELD_WEATHER_DATE = :day")
    fun getSpecificFutureObject(day: Long): FutureWeatherModel

    @Query("SELECT * FROM $FORECAST_DB_TABLE WHERE $FIELD_CITY_ID = :cityId")
    fun getSpecificFutureObjectByCityId(cityId: Int): FutureWeatherModel

//    @Query("UPDATE $FORECAST_DB_TABLE WHERE $CITY_ID = :cityId")
//    suspend fun updateWeather(list: List<FutureWeatherListObjectModel>, cityId: Int)

    @Query("DELETE FROM $FORECAST_DB_TABLE WHERE $FIELD_CITY_ID = :cityId")
    fun deleteCity(cityId: Int)

}