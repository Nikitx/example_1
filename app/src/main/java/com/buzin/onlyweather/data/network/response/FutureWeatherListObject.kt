package com.buzin.onlyweather.data.network.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FutureWeatherListObject(
    @SerializedName("clouds")
    val clouds: Int,
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("snow")
    val snow: Double,
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("temp")
    val temp: Temp,
    @SerializedName("weather")
    val weather: ArrayList<Weather>
) : Parcelable
