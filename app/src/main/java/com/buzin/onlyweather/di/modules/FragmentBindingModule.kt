package com.buzin.onlyweather.di.modules

import com.buzin.onlyweather.di.scopes.ActivityScoped
import com.buzin.onlyweather.weather.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class FragmentBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            // fragments
            DetailWeatherModule::class,
            FutureWeatherModule::class,
            ListWeatherModule::class,
            AddCityDialogModule::class
            // other fragments
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}