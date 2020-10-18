package com.buzin.onlyweather.weather.ui.detail_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.buzin.onlyweather.R
import com.buzin.onlyweather.data.database.current_db.CurrentWeatherModel
import com.buzin.onlyweather.data.network.WEATHER_ICON_URL
import com.buzin.onlyweather.extensions.*
import com.buzin.onlyweather.util.Constants.Companion.DATA_CITY_ID_ARGS
import com.buzin.onlyweather.util.MyUtil
import com.buzin.onlyweather.weather.model.DetailedViewModel
import com.buzin.onlyweather.weather.ui.future_weather.FutureWeatherFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_future_weather.loadingGroup
import javax.inject.Inject


class DetailedWeatherFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailedViewModel
    private var cityId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            cityId =
                arguments!!.getInt(DATA_CITY_ID_ARGS)
        }
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.setCityById(cityId)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = viewModelProvider(viewModelFactory)
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData().observe(viewLifecycleOwner, Observer { viewObject ->
            showProgress(viewObject.progress)
            when {
                viewObject.error -> {
                    if (viewObject.throwable != null) {
                        viewObject.throwable.message?.let { MyUtil.showToast(requireContext(),it) }
                    } else {
                        MyUtil.showToast(requireContext(),getString(R.string.toast_error))
                    }
                }
            }
        })

        setupTitle(getString(R.string.current_weather))

        swipeToRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }

        btn_forecast.setOnClickListener {
            val f = FutureWeatherFragment.newInstance(cityId!!)
            replaceFragment(f)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.detailWeather.observe(viewLifecycleOwner, Observer { city ->
            showCurrentWeather(city)
        })
    }

    private fun showCurrentWeather(currentWeatherModel: CurrentWeatherModel) {
        descriptionTv.text = currentWeatherModel.description
        temperatureTv.text = formatTemperatureStringWithSign(currentWeatherModel.temp)
        minTemperatureTv.text = formatTemperatureString(currentWeatherModel.tempMin, true)
        maxTemperatureTv.text = formatTemperatureString(currentWeatherModel.tempMax, false)
        windTv.text =
            requireContext().getString(R.string.wind, currentWeatherModel.windSpeed.toString())
        setupWeatherIcon(currentWeatherModel.icon)
    }

    private fun setupWeatherIcon(currentWeatherIconURL: String) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = "$WEATHER_ICON_URL$currentWeatherIconURL.png"
        Picasso.get()
            .load(iconURL)
            .into(weatherIv)
    }

    private fun formatTemperatureString(temperatureDouble: Double, isMin: Boolean): String {
        return if (isMin) {
            getString(R.string.minTemp) + formatTemperatureStringWithSign(temperatureDouble)
        } else {
            getString(R.string.maxTemp) + formatTemperatureStringWithSign(temperatureDouble)
        }
    }

    private fun formatTemperatureStringWithSign(temperatureDouble: Double): String {
        return temperatureDouble.roundDoubleToString() + requireContext().getString(R.string.celsiusSign)
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
            swipeToRefresh.isRefreshing = false
        }

    }

    companion object {

        fun newInstance(cityId: Int): DetailedWeatherFragment {
            val f = DetailedWeatherFragment()
            val fragmentBundle = Bundle()
            cityId.let { fragmentBundle.putInt(DATA_CITY_ID_ARGS, it) }
            f.arguments = fragmentBundle
            return f
        }
    }

}
