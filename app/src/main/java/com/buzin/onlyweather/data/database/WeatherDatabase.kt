package com.buzin.onlyweather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherDao
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.database.future_db.FutureWeatherDao
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel


@Database(
    entities = [CurrentWeatherModel::class, FutureWeatherModel::class],
    version = 3, exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}