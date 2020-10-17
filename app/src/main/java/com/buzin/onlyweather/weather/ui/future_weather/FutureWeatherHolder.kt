package com.buzin.onlyweather.weather.ui.future_weather

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.buzin.onlyweather.R
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel
import com.buzin.onlyweather.data.network.WEATHER_ICON_URL
import com.buzin.onlyweather.extensions.getDateFromString
import com.buzin.onlyweather.extensions.roundDoubleToString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_future_weather_list.view.*


class FutureWeatherHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private lateinit var futureWeather: FutureWeatherModel

    fun bindUI(futureWeather: FutureWeatherModel) {
        this.futureWeather = futureWeather

        containerView.dateTv.text = getDateFromString(futureWeather.dt * 1000)
        containerView.temperatureTv.text = getTemperatureString(futureWeather)
        containerView.descriptionTv.text = futureWeather.description
        setupWeatherIcon(futureWeather)
    }

    private fun setupWeatherIcon(futureWeatherModel: FutureWeatherModel) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + futureWeatherModel.icon + ".png"
        Picasso.get()
            .load(iconURL)
            .into(containerView.weatherIv)
    }

    private fun getTemperatureString(futureWeatherModel: FutureWeatherModel): String {
        return containerView.context.getString(
            R.string.tempFormat,
            futureWeatherModel.min.roundDoubleToString(),
            futureWeatherModel.max.roundDoubleToString()
        )
    }
}