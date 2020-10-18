package com.buzin.onlyweather.weather.ui.list_weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzin.onlyweather.R
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel

class ListWeatherAdapter(private val listClickListener: ListWeatherClickListener) :
    RecyclerView.Adapter<ListWeatherHolder>() {

    private var citiesList = emptyList<CurrentWeatherModel>() // Cached copy of cities

    internal fun setCitiesList(citiesList: MutableList<CurrentWeatherModel>) {
        this.citiesList = citiesList
        notifyDataSetChanged()
    }
    private var currentWeatherList: List<CurrentWeatherModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWeatherHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_weather_list, parent, false)
        return ListWeatherHolder(itemView)
    }

    override fun getItemCount(): Int = citiesList.size


    override fun onBindViewHolder(holder: ListWeatherHolder, position: Int) {
        holder.bindUI(citiesList[position])
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                listClickListener.invoke(citiesList[holder.adapterPosition].cityId)
            }
        }
    }
}

typealias ListWeatherClickListener = (Int) -> Unit
