package com.buzin.onlyweather.di

import com.buzin.onlyweather.MyApplication
import com.buzin.onlyweather.di.modules.AppModule
import com.buzin.onlyweather.di.modules.FragmentBindingModule
import com.buzin.onlyweather.di.modules.NetworkModule
import com.buzin.onlyweather.di.modules.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main component of the app, created and persisted in the Application class.
 *
 * Whenever a new module is created, it should be added to the list of modules.
 * [AndroidSupportInjectionModule] is the module from Dagger.Android that helps with the
 * generation and location of subcomponents.
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        FragmentBindingModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyApplication>()
}