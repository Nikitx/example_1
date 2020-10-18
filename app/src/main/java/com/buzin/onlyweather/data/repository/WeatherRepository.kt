package com.buzin.onlyweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buzin.onlyweather.R
import com.buzin.onlyweather.MyApplication
import com.buzin.onlyweather.data.database.WeatherDatabase
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.database.current_db.fromApiDataToModelWeather
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel
import com.buzin.onlyweather.data.database.future_db.createArrayFromApiWeatherList
import com.buzin.onlyweather.data.network.ApiWeatherInterface
import com.buzin.onlyweather.data.network.NetworkProvider
import com.buzin.onlyweather.data.network.response.CurrentWeather
import com.buzin.onlyweather.data.network.response.FutureWeather
import com.buzin.onlyweather.data.network.result.NetworkCurrentWeatherResult
import com.buzin.onlyweather.data.network.result.NetworkFutureWeatherResult
import com.buzin.onlyweather.util.Constants.Companion.AMOUNT_OF_DAYS
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


interface WeatherRepository {
    fun getSpecificWeatherModel(dt: Long?): LiveData<FutureWeatherModel>?
}

@Singleton
class WeatherRepositoryProvider @Inject constructor(
    private val apiWeatherInterface: ApiWeatherInterface,
    private val weatherDatabase: WeatherDatabase,
    private val networkProvider: NetworkProvider,
    private val myApplication: MyApplication
) : WeatherRepository {

    // дергаем во вью модели в инит и рефреш
    suspend fun getCurrentWeatherByCity(cityName: String): NetworkCurrentWeatherResult {
        return try {
            if (networkProvider.isDeviceOnline()) {
                var networkResult: NetworkCurrentWeatherResult? = null
                apiWeatherInterface.getCurrentWeatherByCityName(
                    cityName
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CurrentWeather> {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(result: CurrentWeather) {
                            CoroutineScope(Dispatchers.IO).launch {
                                weatherDatabase.currentWeatherDao()
                                    .insert(result.fromApiDataToModelWeather())
                                networkResult =
                                    NetworkCurrentWeatherResult.Success(result.fromApiDataToModelWeather())
                            }
                        }

                        override fun onError(e: Throwable) {
                            networkResult = NetworkCurrentWeatherResult.CommunicationError(
                                Throwable(e.cause)
                            )
                        }
                    })
                networkResult!!
            } else {
                NetworkCurrentWeatherResult.CommunicationError(
                    Throwable(
                        myApplication.getString(R.string.toast_error_internet_access)
                    )
                )
            }
        } catch (e: Exception) {
            NetworkCurrentWeatherResult.CommunicationError(e)
        }
    }

    // в апи рекомендуют запрашивать но id
    suspend fun getCurrentWeatherByCity(cityId: Int): NetworkCurrentWeatherResult {
        return try {
            if (networkProvider.isDeviceOnline()) {
                var networkResult: NetworkCurrentWeatherResult? = null
                apiWeatherInterface.getCurrentWeatherById(
                    cityId
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CurrentWeather> {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(result: CurrentWeather) {
                            CoroutineScope(Dispatchers.IO).launch {
                                weatherDatabase.currentWeatherDao()
                                    .insert(result.fromApiDataToModelWeather())
                                networkResult =
                                    NetworkCurrentWeatherResult.Success(result.fromApiDataToModelWeather())
                            }
                        }

                        override fun onError(e: Throwable) {
                            networkResult = NetworkCurrentWeatherResult.CommunicationError(
                                Throwable(e.cause)
                            )
                        }
                    })
                networkResult!!
            } else {
                NetworkCurrentWeatherResult.CommunicationError(
                    Throwable(
                        myApplication.getString(R.string.toast_error_internet_access)
                    )
                )
            }
        } catch (e: Exception) {
            NetworkCurrentWeatherResult.CommunicationError(e)
        }
    }

    suspend fun getFutureWeatherByCity(cityId: Int): NetworkFutureWeatherResult {
        return try {
            if (networkProvider.isDeviceOnline()) {
                var networkResult: NetworkFutureWeatherResult? = null
                apiWeatherInterface.getFutureWeatherById(
                    cityId, AMOUNT_OF_DAYS
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<FutureWeather> {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(result: FutureWeather) {
                            CoroutineScope(Dispatchers.IO).launch {
                                weatherDatabase.futureWeatherDao().deleteCity(cityId)
                                weatherDatabase.futureWeatherDao().insertFutureListAll(
                                    createArrayFromApiWeatherList(
                                        result.listWeather,
                                        result.futureCityData
                                    )
                                )
                                networkResult =
                                    NetworkFutureWeatherResult.Success(
                                        createArrayFromApiWeatherList(
                                            result.listWeather,
                                            result.futureCityData
                                        )
                                    )
                            }
                        }

                        override fun onError(e: Throwable) {
                            networkResult =
                                NetworkFutureWeatherResult.CommunicationError(Throwable(e.cause))
                        }
                    })
                networkResult!!
            } else {
                NetworkFutureWeatherResult.CommunicationError(
                    Throwable(
                        myApplication.getString(R.string.toast_error_internet_access)
                    )
                )
            }
        } catch (e: Exception) {
            NetworkFutureWeatherResult.CommunicationError(e)
        }
    }

    suspend fun getCurrentWeatherListFromDB(): List<CurrentWeatherModel>? {
        return weatherDatabase.currentWeatherDao().getCityList()
    }

    fun getAllCities() = weatherDatabase.currentWeatherDao().getAllCities()

    fun getAllDaysForecast(cityId: Int) =
        weatherDatabase.futureWeatherDao().getAllDaysForecastList(cityId)

    fun getCity(cityId: Int) = weatherDatabase.currentWeatherDao().getCityById(cityId)

    private val specificWeatherMutableLiveData = MutableLiveData<FutureWeatherModel>()

    override fun getSpecificWeatherModel(dt: Long?): LiveData<FutureWeatherModel>? {
        return specificWeatherMutableLiveData
    }
}