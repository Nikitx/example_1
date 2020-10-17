package com.buzin.onlyweather.weather.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buzin.onlyweather.data.network.result.NetworkCurrentWeatherResult
import com.buzin.onlyweather.data.repository.WeatherRepositoryProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AddCityDialogViewModel @Inject constructor(
    private val weatherRepositoryProvider: WeatherRepositoryProvider,
) : ViewModel() {

    private val _isNeedSuccessToast = MutableLiveData(false)
    val isNeedSuccessToast: LiveData<Boolean> = _isNeedSuccessToast
    private val _isNeedErrorToast = MutableLiveData(false)
    val isNeedErrorToast: LiveData<Boolean> = _isNeedErrorToast

    suspend fun addNewCityByName(cityName: String) {
        if (cityName.trim().isNotEmpty()) {
            CoroutineScope(Dispatchers.Main).launch {
                when (val result: NetworkCurrentWeatherResult =
                    weatherRepositoryProvider.getCurrentWeatherByCity(cityName.toLowerCase(Locale.ROOT))) {
                    is NetworkCurrentWeatherResult.Success -> {
                        _isNeedSuccessToast.apply { value = true }
                    }
                    is NetworkCurrentWeatherResult.CommunicationError -> {
                        _isNeedErrorToast.apply { value = true }
                    }
                }
            }
        }
    }

    fun needSuccessToast() {
        _isNeedSuccessToast.apply { value = false }
    }

    fun needErrorToast() {
        _isNeedErrorToast.apply { value = false }
    }

}