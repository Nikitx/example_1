package com.buzin.onlyweather.weather.ui.future_weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzin.onlyweather.R
import com.buzin.onlyweather.data.database.future_db.FutureWeatherModel

class FutureWeatherAdapter: RecyclerView.Adapter<FutureWeatherHolder>() {

    private var futureWeatherList: List<FutureWeatherModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureWeatherHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_future_weather_list, parent, false)
        return FutureWeatherHolder(itemView)
    }

    override fun getItemCount(): Int = futureWeatherList.size

    override fun onBindViewHolder(holder: FutureWeatherHolder, position: Int) {
        holder.bindUI(futureWeatherList[position])
    }

    fun updateWeather(futureWeatherList: List<FutureWeatherModel>) {
        this.futureWeatherList = futureWeatherList
        notifyDataSetChanged()
    }
}
