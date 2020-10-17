package com.buzin.onlyweather.data.network.response

import com.google.gson.annotations.SerializedName

data class FutureWeather(
    @SerializedName("cod")
    val cod: String,

    @SerializedName("list")
    val listWeather: ArrayList<FutureWeatherListObject>,

    @SerializedName("message")
    val message: Double,

    @SerializedName("city")
    val futureCityData: FutureCityData

)