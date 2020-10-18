package com.buzin.onlyweather.di.modules

import android.content.Context
import com.buzin.onlyweather.MyApplication
import com.buzin.onlyweather.data.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideContext(application: MyApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesWeatherDatabase(context: Context): WeatherDatabase = WeatherDatabase.buildDatabase(context)

    }