package com.buzin.onlyweather.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.buzin.onlyweather.data.database.FutureWeatherConverter.GsonProvider.listFromJson
import com.buzin.onlyweather.data.database.FutureWeatherConverter.GsonProvider.listToJson
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel
import com.buzin.onlyweather.data.network.response.Weather
import java.lang.reflect.Type

const val DATABASE_NAME = "database.db"

const val CURRENT_DB_TABLE = "current_weather_table"
const val FORECAST_DB_TABLE = "forcast_weather_table"

const val FIELD_CITY_ID = "cityId"
const val FIELD_WEATHER_DATE = "dt"


class WeatherConverter {

    @TypeConverter
    fun fromWeatherList(value: List<Weather>): String {
        val type = object : TypeToken<List<Weather>>() {}.type
        return listToJson(value, type)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return listFromJson(value, type)
    }
}

class FutureWeatherConverter {
    @TypeConverter
    fun fromFeatureWeatherList(value: List<FutureWeatherModel>): String {
        val type = object : TypeToken<List<FutureWeatherModel>>() {}.type
        return listToJson(value, type)
    }

    @TypeConverter
    fun toFeatureWeatherList(value: String): List<FutureWeatherModel> {
        val type = object : TypeToken<List<FutureWeatherModel>>() {}.type
        return listFromJson(value, type)
    }

    object GsonProvider {
        private var gson: Gson? = null

        private fun getGson(): Gson {
            if (gson == null) {
                gson = Gson()
                return gson as Gson
            }
            return gson as Gson
        }

        fun <E> listFromJson(o: String, listType: Type): List<E> {
            return getGson().fromJson(o, listType)
        }

        fun <T> listToJson(list: List<T>, listType: Type): String {
            return getGson().toJson(list, listType)
        }
    }
}