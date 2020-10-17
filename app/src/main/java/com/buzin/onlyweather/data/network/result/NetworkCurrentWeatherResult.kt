package com.buzin.onlyweather.data.network.result

import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel

sealed class NetworkCurrentWeatherResult {
    data class Success(val currentWeatherModel: CurrentWeatherModel?) : NetworkCurrentWeatherResult()
    data class CommunicationError(val cause: Throwable?) : NetworkCurrentWeatherResult()
}