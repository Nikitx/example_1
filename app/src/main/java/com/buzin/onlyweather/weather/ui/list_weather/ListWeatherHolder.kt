package com.buzin.onlyweather.weather.ui.list_weather

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.buzin.onlyweather.R
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.network.WEATHER_ICON_URL
import com.buzin.onlyweather.extensions.getDateFromString
import com.buzin.onlyweather.extensions.roundDoubleToString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_future_weather_list.view.dateTv
import kotlinx.android.synthetic.main.item_future_weather_list.view.descriptionTv
import kotlinx.android.synthetic.main.item_future_weather_list.view.temperatureTv
import kotlinx.android.synthetic.main.item_future_weather_list.view.weatherIv
import kotlinx.android.synthetic.main.item_main_weather_list.view.*


class ListWeatherHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    private lateinit var currentWeatherListObject: CurrentWeatherModel

    fun bindUI(currentWeatherListObject: CurrentWeatherModel) {
        this.currentWeatherListObject = currentWeatherListObject
        containerView.dateTv.text = getDateFromString(currentWeatherListObject.dt)
        containerView.temperatureTv.text = getTemperatureString(currentWeatherListObject)
        containerView.descriptionTv.text = currentWeatherListObject.description

        containerView.cityNameTv.text = currentWeatherListObject.name

        setupWeatherIcon(currentWeatherListObject)
    }

    private fun setupWeatherIcon(currentWeatherModel: CurrentWeatherModel) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + currentWeatherModel.icon + ".png"

        Picasso.get() // todo переделать на Glide
            .load(iconURL)
            .into(containerView.weatherIv)
    }

    private fun getTemperatureString(currentWeatherModel: CurrentWeatherModel): String {
        return containerView.context.getString(
            R.string.tempFormat,
            currentWeatherModel.tempMin.roundDoubleToString(),
            currentWeatherModel.tempMax.roundDoubleToString()
        )
    }
}