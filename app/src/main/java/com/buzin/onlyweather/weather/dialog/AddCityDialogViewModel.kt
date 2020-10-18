package com.buzin.onlyweather.weather.dialog

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


    suspend fun addNewCityByName(cityName: String) {
        if (cityName.trim().isNotEmpty()) {
            CoroutineScope(Dispatchers.Main).launch {
                when (val result: NetworkCurrentWeatherResult =
                    weatherRepositoryProvider.getCurrentWeatherByCity(cityName.toLowerCase(Locale.ROOT))
                )
                {
                    is NetworkCurrentWeatherResult.Success -> {

                    }
                    is NetworkCurrentWeatherResult.CommunicationError -> {

                    }
                }
            }
        }
    }


}