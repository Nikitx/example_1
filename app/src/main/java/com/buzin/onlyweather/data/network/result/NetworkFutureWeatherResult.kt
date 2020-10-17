package com.buzin.onlyweather.data.network.result

import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel

sealed class NetworkFutureWeatherResult {
    data class Success(val futureWeatherList: List<FutureWeatherModel>?) : NetworkFutureWeatherResult()
    data class CommunicationError(val cause: Throwable?) : NetworkFutureWeatherResult()
}