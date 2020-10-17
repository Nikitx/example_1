package com.buzin.onlyweather.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buzin.onlyweather.WeatherApplication
import com.buzin.onlyweather.data.database.WeatherDatabase
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.network.result.NetworkCurrentWeatherResult
import com.buzin.onlyweather.data.repository.WeatherRepositoryProvider
import com.buzin.onlyweather.util.MySettings
import com.buzin.onlyweather.util.WeatherUtil
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ListWeatherViewModel @Inject constructor(
    private val repository: WeatherRepositoryProvider,
    private val mySettings: MySettings,
    private val weatherDatabase: WeatherDatabase,
    private val weatherApplication: WeatherApplication
) : BaseWeatherViewModel<List<CurrentWeatherModel>>() {

    private val mutableLiveDataList = MutableLiveData<List<CurrentWeatherModel>>()
    private val listWeather = mutableListOf<CurrentWeatherModel>()
    private val setWeather = hashSetOf<String>()
    var allCities: LiveData<MutableList<CurrentWeatherModel>>

    private val _isDataRefreshed = MutableLiveData(false)
    val isDataRefreshed: LiveData<Boolean> = _isDataRefreshed

    private val _needToastAboutRefresh = MutableLiveData(false)
    val needToastAboutRefresh: LiveData<Boolean> = _needToastAboutRefresh


    private fun getCurrentWeatherModel(): LiveData<List<CurrentWeatherModel>>? {
        return mutableLiveDataList
    }

    override fun refreshData() {
        getResult()
    }

    override fun createLiveData(): LiveData<List<CurrentWeatherModel>>? =
        getCurrentWeatherModel()

    // кэшируем сисок id из базы с проверкой у всех ли есть айди, если нет то получаем отдельно
    private fun getResult() {
        scope.launch {

            repository.getCurrentWeatherListFromDB()?.forEach {

                // TODO тут можно переделать на получение списка городов сразу по id, если будет время
                //  передалаю. Апи позволяет получать список городов по спиcку id.
                when (val currentWeatherResult: NetworkCurrentWeatherResult =
                    repository.getCurrentWeatherByCity(it.name.toLowerCase(Locale.ROOT))) {

                    is NetworkCurrentWeatherResult.Success -> {
                        mySettings.lastSync(System.currentTimeMillis())
                        _isDataRefreshed.apply { value = true }

                        mutableLiveData.postValue(
                            ViewObject(
                                //data = null,
                                progress = false,
                                error = false,
                                throwable = null
                            )
                        )
                    }

                    is NetworkCurrentWeatherResult.CommunicationError -> {
                        mutableLiveData.postValue(
                            ViewObject(
                                //data = null,
                                progress = false,
                                error = true,
                                throwable = currentWeatherResult.cause
                            )
                        )
                    }


                }
            }

            // добавляем в  лив дату
//            mutableLiveDataList.postValue(listWeather)
        }
    }

    fun refreshedData() {
        _isDataRefreshed.apply { value = false }
    }


    private fun is24ago() {
        _needToastAboutRefresh.apply {
            value = ((System.currentTimeMillis() - mySettings.lastSync()) / 1000 / 60 / 60) > 24
            WeatherUtil.toLog("${((System.currentTimeMillis() - mySettings.lastSync()) / 1000 / 60 / 60)}")
        }
    }

    private suspend fun addDemoCities() {
        if (mySettings.needToAddDemoCities()) {

            val moscow = CurrentWeatherModel(
                "stations",
                0,
                0.0,
                0.0,
                0.0,
                "Moscow",
                0,
                "description",
                "04n",
                "Clouds",
                0.0,
                524901
            )
            weatherDatabase.currentWeatherDao().insert(moscow)

            val piter = CurrentWeatherModel(
                "stations",
                0,
                0.0,
                0.0,
                0.0,
                "Saint Petersburg",
                0,
                "description",
                "09n",
                "Rain",
                0.0,
                498817
            )
            weatherDatabase.currentWeatherDao().insert(piter)

            mySettings.setNeedToAddDemoCities(false)
        }
    }

    init {

        if (mySettings.needToAddDemoCities()) {
            scope.launch {
                addDemoCities()
            }
        }

        allCities = repository.getAllCities()

        is24ago()
        fetchData()
        getResult()
    }
}


