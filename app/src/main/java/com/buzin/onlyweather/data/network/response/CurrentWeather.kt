package com.buzin.onlyweather.data.network.response

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("base")
    val base: String,

    @SerializedName("coord")
    val coord: Coord,

    @SerializedName("dt")
    val dt: Double,

    @SerializedName("main")
    val main: Main,

    @SerializedName("name")
    val name: String,

    @SerializedName("visibility")
    val visibility: Int,

    @SerializedName("weather")
    val weather: ArrayList<Weather>,

    @SerializedName("wind")
    val wind: Wind,

    @SerializedName("id")
    val cityId: Int
)