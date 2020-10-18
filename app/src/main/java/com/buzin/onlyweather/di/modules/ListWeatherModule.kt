package com.buzin.onlyweather.di.modules

import androidx.lifecycle.ViewModel
import com.buzin.onlyweather.di.utils.ViewModelKey
import com.buzin.onlyweather.di.scopes.FragmentScope
import com.buzin.onlyweather.weather.model.ListViewModel
import com.buzin.onlyweather.weather.ui.list_weather.ListWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ListWeatherModule {

    /**
     * Generates an [AndroidInjector] for the [ListWeatherFragment].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeListWeatherFragment(): ListWeatherFragment

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListWeatherViewModel(viewModel: ListViewModel): ViewModel
}