package com.buzin.onlyweather.di.modules

import androidx.lifecycle.ViewModel
import com.buzin.onlyweather.di.utils.ViewModelKey
import com.buzin.onlyweather.di.scopes.FragmentScope
import com.buzin.onlyweather.weather.model.FutureViewModel
import com.buzin.onlyweather.weather.ui.future_weather.FutureWeatherFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class FutureWeatherModule {

    /**
     * Generates an [AndroidInjector] for the [FutureWeatherFragment].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeFuttureWeatherFragment(): FutureWeatherFragment

    @Binds
    @IntoMap
    @ViewModelKey(FutureViewModel::class)
    abstract fun bindAgendaViewModel(viewModel: FutureViewModel): ViewModel
}