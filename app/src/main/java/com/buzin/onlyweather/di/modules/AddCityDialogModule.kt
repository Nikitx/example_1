package com.buzin.onlyweather.di.modules

import androidx.lifecycle.ViewModel
import com.buzin.onlyweather.di.scopes.FragmentScope
import com.buzin.onlyweather.di.utils.ViewModelKey
import com.buzin.onlyweather.weather.dialog.AddCityDialog
import com.buzin.onlyweather.weather.dialog.AddCityDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class AddCityDialogModule {

    /**
     * Generates an [AndroidInjector] for the [AddCityDialog].
     */
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeAddCityDialog(): AddCityDialog

    @Binds
    @IntoMap
    @ViewModelKey(AddCityDialogViewModel::class)
    abstract fun bindAgendaViewModel(viewModel: AddCityDialogViewModel): ViewModel
}