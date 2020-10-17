package com.buzin.onlyweather

import com.buzin.onlyweather.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class WeatherApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
