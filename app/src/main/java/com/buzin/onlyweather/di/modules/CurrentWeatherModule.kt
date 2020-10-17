package com.buzin.onlyweather.di.modules

import androidx.lifecycle.ViewModel
import com.buzin.onlyweather.di.utils.ViewModelKey
import com.buzin.onlyweather.di.scopes.FragmentScope
import com.buzin.onlyweather.weather.model.DetailedWeatherViewModel
import com.buzin.onlyweather.weather.ui.detail_weather.DetailedWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class CurrentWeatherModule {

    /**
     * Generates an [AndroidInjector] for the [DetailedWeatherFragment].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeCurrentWeatherFragment(): DetailedWeatherFragment

    @Binds
    @IntoMap
    @ViewModelKey(DetailedWeatherViewModel::class)
    abstract fun bindCurrentWeatherViewModel(viewModel: DetailedWeatherViewModel): ViewModel
}