package com.buzin.onlyweather.weather.ui.list_weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzin.onlyweather.R
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
//import com.buzin.onlyweather.data.database.future_db.FutureWeatherListObjectModel

class ListWeatherAdapter(private val listClickListener: ListWeatherClickListener) :
    RecyclerView.Adapter<ListWeatherHolder>() {

    private var citiesList = emptyList<CurrentWeatherModel>() // Cached copy of cities

//    private var futureWeatherList: List<FutureWeatherListObjectModel> = listOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWeatherHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_main_weather_list, parent, false)
//        return ListWeatherHolder(itemView)
//    }
//
//    override fun getItemCount(): Int = futureWeatherList.size
//
//    override fun onBindViewHolder(weatherHolder: ListWeatherHolder, position: Int) {
//        weatherHolder.bindUI(futureWeatherList[position])
//        weatherHolder.itemView.setOnClickListener {
//            if (weatherHolder.adapterPosition != RecyclerView.NO_POSITION) {
//                listClickListener.invoke(futureWeatherList[weatherHolder.adapterPosition].dt) //date in mills
//            }
//        }
//    }
//
//    fun updateWeather(futureWeatherList: List<FutureWeatherListObjectModel>) {
//        this.futureWeatherList = futureWeatherList
//        notifyDataSetChanged()
//    }
//}

    internal fun setCitiesList(citiesList: MutableList<CurrentWeatherModel>) {
        this.citiesList = citiesList
        notifyDataSetChanged()
//        Log.e("list", "list size ==> " + contactList.size )
    }
    private var currentWeatherList: List<CurrentWeatherModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWeatherHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_weather_list, parent, false)
        return ListWeatherHolder(itemView)
    }

//    override fun getItemCount(): Int = currentWeatherList.size
    override fun getItemCount(): Int = citiesList.size


    override fun onBindViewHolder(holder: ListWeatherHolder, position: Int) {
        holder.bindUI(citiesList[position])
//        weatherHolder.bindUI(currentWeatherList[position])
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
//                listClickListener.invoke(currentWeatherList[weatherHolder.adapterPosition].dt.toLong()) //date in mills
                listClickListener.invoke(citiesList[holder.adapterPosition].cityId)
            }
        }
    }

//    fun updateWeather(currentWeatherList: List<CurrentWeatherModel>) {
//        this.currentWeatherList = currentWeatherList
//        notifyDataSetChanged()
//    }
}

typealias ListWeatherClickListener = (Int) -> Unit
//typealias ListWeatherClickListener = (Long) -> Unit