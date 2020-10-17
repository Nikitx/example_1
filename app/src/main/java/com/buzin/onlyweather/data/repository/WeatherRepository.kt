package com.buzin.onlyweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buzin.onlyweather.R
import com.buzin.onlyweather.WeatherApplication
import com.buzin.onlyweather.data.database.WeatherDatabase
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.database.current_db.fromApiDataToModelWeather
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel
import com.buzin.onlyweather.data.database.future_db.createArrayFromApiWeatherList
import com.buzin.onlyweather.data.network.ApiWeatherInterface
import com.buzin.onlyweather.data.network.NetworkProvider
import com.buzin.onlyweather.data.network.result.NetworkCurrentWeatherResult
import com.buzin.onlyweather.data.network.result.NetworkFutureWeatherResult
import com.buzin.onlyweather.util.Constants.Companion.AMOUNT_OF_DAYS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    private val weatherApplication: WeatherApplication
) : WeatherRepository {


    // дергаем во вью модели в инит и рефреш
    suspend fun getCurrentWeatherByCity(cityName: String): NetworkCurrentWeatherResult {
        return try {
            if (networkProvider.isDeviceOnline()) {
                val fromApiDataToModelWeather = apiWeatherInterface.getCurrentWeatherByCityName(
                    cityName
                ).await()
                weatherDatabase.currentWeatherDao()
                    .insert(fromApiDataToModelWeather.fromApiDataToModelWeather())
                NetworkCurrentWeatherResult.Success(fromApiDataToModelWeather.fromApiDataToModelWeather())
            } else {
                NetworkCurrentWeatherResult.CommunicationError(
                    Throwable(
                        weatherApplication.getString(R.string.toast_error_internet_access)
                    )
                )
            }
        } catch (e: Exception) {
            NetworkCurrentWeatherResult.CommunicationError(e)
        }
    }

    // в апи рекомендуют запрашивать но id, а не по координатам или названию города
    suspend fun getCurrentWeatherByCity(cityid: Int): NetworkCurrentWeatherResult {
        return try {
            if (networkProvider.isDeviceOnline()) {
                val fromApiDataToModelWeather = apiWeatherInterface.getCurrentWeatherById(
                    cityid
                ).await()
                weatherDatabase.currentWeatherDao()
                    .insert(fromApiDataToModelWeather.fromApiDataToModelWeather())
                NetworkCurrentWeatherResult.Success(fromApiDataToModelWeather.fromApiDataToModelWeather())
            } else {
                NetworkCurrentWeatherResult.CommunicationError(
                    Throwable(
                        weatherApplication.getString(R.string.toast_error_internet_access)
                    )
                )
            }
        } catch (e: Exception) {
            NetworkCurrentWeatherResult.CommunicationError(e)
        }
    }

    suspend fun getCurrentWeatherListFromDB(): List<CurrentWeatherModel>? {
        return weatherDatabase.currentWeatherDao().getCityList()
    }

    fun getAllCities() = weatherDatabase.currentWeatherDao().getAllCities()

    fun getAllDaysForecast(cityId: Int) =
        weatherDatabase.futureWeatherDao().getAllDaysForecastList(cityId)

    fun getCity(cityId: Int) = weatherDatabase.currentWeatherDao().getCityById(cityId)

    suspend fun getFutureWeatherByCity(cityId: Int): NetworkFutureWeatherResult {
        return try {
            if (networkProvider.isDeviceOnline()) {
                val fromApiDataToFutureModelWeatherList = createArrayFromApiWeatherList(
                    apiWeatherInterface.getFutureWeatherById(
                        cityId,
                        AMOUNT_OF_DAYS
                    ).await().listWeather,
                    apiWeatherInterface.getFutureWeatherById(
                        cityId,
                        AMOUNT_OF_DAYS
                    ).await().futureCityData
                )

                CoroutineScope(Dispatchers.IO).launch {
                    weatherDatabase.futureWeatherDao().deleteCity(cityId)

                    weatherDatabase.futureWeatherDao()
                        .insertFutureListAll(fromApiDataToFutureModelWeatherList)
                }

                NetworkFutureWeatherResult.Success(fromApiDataToFutureModelWeatherList)
            } else {
//                NetworkFutureWeatherResult.Success(getListFutureWeatherFromDB(cityId))
                NetworkFutureWeatherResult.CommunicationError(
                    Throwable(
                        weatherApplication.getString(R.string.toast_error_internet_access)
                    )
                )
            }
        } catch (e: Exception) {
            NetworkFutureWeatherResult.CommunicationError(e)
        }
    }

    private val specificWeatherMutableLiveData = MutableLiveData<FutureWeatherModel>()

    override fun getSpecificWeatherModel(dt: Long?): LiveData<FutureWeatherModel>? {
        return specificWeatherMutableLiveData
    }
}