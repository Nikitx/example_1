package com.buzin.onlyweather.di.modules

import com.buzin.onlyweather.data.network.API_KEY
import com.buzin.onlyweather.data.network.ApiWeatherInterface
import com.buzin.onlyweather.data.network.UNITS
import com.buzin.onlyweather.data.network.BASE_URL
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    private val requestInterceptor = Interceptor { chain ->

        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("appid", API_KEY)
            .addQueryParameter("units", UNITS)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor chain.proceed(request)
    }

    /**
     * Provide the Api Service implementation
     * retrofit the Retrofit object used to instantiate the service
     * @return the api service implementation
     */
    @Provides
    @Singleton
    fun createApiInterface(): ApiWeatherInterface {
        val okClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(requestInterceptor) // add key as query parameter for all calls
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okClient)
            .build()
        return retrofit.create(ApiWeatherInterface::class.java)
    }
}