package com.buzin.onlyweather.di.modules

import androidx.lifecycle.ViewModelProvider
import com.buzin.onlyweather.di.utils.WeatherViewModelFactory
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: WeatherViewModelFactory):
            ViewModelProvider.Factory
}