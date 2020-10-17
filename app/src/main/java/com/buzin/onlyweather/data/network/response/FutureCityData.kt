package com.buzin.onlyweather.data.network.response

import com.google.gson.annotations.SerializedName

data class FutureCityData(

    @SerializedName("id")
    val cityId: Int,

    @SerializedName("name")
    val cityName: String,

    @SerializedName("country")
    val country: String

)