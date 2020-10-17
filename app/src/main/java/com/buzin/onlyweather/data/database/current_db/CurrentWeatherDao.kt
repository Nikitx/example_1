package com.buzin.onlyweather.data.database.current_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buzin.onlyweather.data.database.FIELD_CITY_ID
import com.buzin.onlyweather.data.database.CURRENT_DB_TABLE

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherEntry: CurrentWeatherModel)

    @Query("select * from $CURRENT_DB_TABLE")
    suspend fun getCurrentWeather(): CurrentWeatherModel
//    @Query("select * from $CURRENT_DB_TABLE where id = $CURRENT_WEATHER_ID")
//    suspend fun getCurrentWeather(): CurrentWeatherModel


    @Query("SELECT * FROM $CURRENT_DB_TABLE")
    suspend fun getCityList(): List<CurrentWeatherModel>

    @Query("SELECT * FROM $CURRENT_DB_TABLE")
    fun getAllCities(): LiveData<MutableList<CurrentWeatherModel>>

    @Query("SELECT * FROM $CURRENT_DB_TABLE WHERE $FIELD_CITY_ID = :cityId")
    fun getCityByIdLive(cityId: Int): LiveData<CurrentWeatherModel>


    @Query("DELETE FROM $CURRENT_DB_TABLE")
    suspend fun deleteAllCities()

//    @Query("SELECT * FROM $CURRENT_DB_TABLE WHERE $CITY_ID = :list")
//    suspend fun getIdListOfCities(list: List<Int>): List<CurrentWeatherModel>

    @Query("SELECT * FROM $CURRENT_DB_TABLE WHERE $FIELD_CITY_ID = :cityId")
     fun getCityById(cityId: Int): LiveData<CurrentWeatherModel>

//    @Query("SELECT * FROM " + PHONE_CODE_TABLE + " WHERE " + PHONE_CODE_BLOCKED + " = :isBlocked")
//    fun getBlockedPhoneCode(isBlocked: Boolean): MutableList<PhoneCode>


//    @Query("DELETE FROM ${CURRENT_DB_TABLE} WHERE ${PHONE_CODE_ID} = :id")
//    fun deleteCity(id: String)
//
//    @Query("UPDATE " + CURRENT_DB_TABLE + " SET " + PHONE_CODE_BLOCKED + " = :isBlocked" + " WHERE " + PHONE_CODE_ID + " = :id ")
//    suspend fun updateWeather(id: String, isBlocked: Boolean)


}