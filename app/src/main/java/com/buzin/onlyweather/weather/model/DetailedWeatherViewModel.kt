package com.buzin.onlyweather.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.network.result.NetworkCurrentWeatherResult
import com.buzin.onlyweather.data.repository.WeatherRepositoryProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailedWeatherViewModel @Inject constructor(
    private val repository: WeatherRepositoryProvider,
) : BaseWeatherViewModel<CurrentWeatherModel>() {

    private var cityId: Int? = 0
    lateinit var detailWeather: LiveData<CurrentWeatherModel>

    private val currentWeatherMutableLiveData = MutableLiveData<CurrentWeatherModel>()

    private fun getCurrentWeatherModel(): LiveData<CurrentWeatherModel>? {
        return currentWeatherMutableLiveData
    }

    override fun refreshData() {
        getResult()
    }

    fun setCityById(cityId: Int?) {
        this.cityId = cityId
        refreshData()
        detailWeather = repository.getCity(cityId!!)

    }

    init {
        fetchData()
        getResult()
    }

    private fun getResult() {
        scope.launch {
            when (val result: NetworkCurrentWeatherResult =
                repository.getCurrentWeatherByCity(cityId!!)) {
                is NetworkCurrentWeatherResult.Success -> {
                    currentWeatherMutableLiveData.postValue(result.currentWeatherModel)
                }
                is NetworkCurrentWeatherResult.CommunicationError -> {
                    mutableLiveData.postValue(
                        ViewObject(
//                            data = null,
                            progress = false,
                            error = true,
                            throwable = result.cause
                        )
                    )
                }
            }

        }
    }


    override fun createLiveData(): LiveData<CurrentWeatherModel>? =
        getCurrentWeatherModel()

    companion object {
        const val NEED_TO_UPDATE = true
    }

    init {
    }
}
