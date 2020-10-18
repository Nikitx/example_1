package com.buzin.onlyweather.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel
import com.buzin.onlyweather.data.network.result.NetworkFutureWeatherResult
import com.buzin.onlyweather.data.repository.WeatherRepositoryProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class FutureViewModel @Inject constructor(
    private val repository: WeatherRepositoryProvider,
) : BaseViewModel<List<FutureWeatherModel>>() {

    private val futureWeatherMutableLiveData = MutableLiveData<List<FutureWeatherModel>>()
    private var cityId: Int? = 0
    lateinit var allDaysForecast: LiveData<MutableList<FutureWeatherModel>>

    private fun getFutureWeatherModel(): LiveData<List<FutureWeatherModel>>? {
        return futureWeatherMutableLiveData
    }

    fun setCityById(cityId: Int?) {
        this.cityId = cityId
        allDaysForecast = repository.getAllDaysForecast(cityId!!)

    }

    override fun refreshData() {
        getResult()
    }

    init {
        fetchData()
        getResult()
    }

    private fun getResult() {
        scope.launch {
            when (val result: NetworkFutureWeatherResult =
                repository.getFutureWeatherByCity(cityId!!)) {
                is NetworkFutureWeatherResult.Success -> {
                    futureWeatherMutableLiveData.postValue(result.futureWeatherList)
                }
                is NetworkFutureWeatherResult.CommunicationError -> {
                    mutableLiveData.postValue(
                        ViewObject(
                            progress = false,
                            error = true,
                            throwable = result.cause
                        )
                    )
                }
            }
        }
    }

    override fun createLiveData(): LiveData<List<FutureWeatherModel>>? =
        getFutureWeatherModel()
}