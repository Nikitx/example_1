package com.buzin.onlyweather.weather.ui.future_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzin.onlyweather.R
import com.buzin.onlyweather.extensions.setupTitle
import com.buzin.onlyweather.extensions.viewModelProvider
import com.buzin.onlyweather.util.Constants.Companion.DATA_CITY_ID_ARGS
import com.buzin.onlyweather.util.WeatherUtil
import com.buzin.onlyweather.weather.model.FutureWeatherViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.future_weather_fragment.*
import javax.inject.Inject

class FutureWeatherFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FutureWeatherViewModel
    private lateinit var mAdapter: FutureWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var cityId: Int? = null
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
        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFutureWeatherListView()

        setupTitle(getString(R.string.future_weather))

        swipeToRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Подписались на БД получаем данные после обновлений БД
        viewModel.allDaysForecast.observe(viewLifecycleOwner, Observer { result ->
            result.let { mAdapter.updateWeather(it) }
        })

        viewModel.liveData().observe(viewLifecycleOwner, Observer { viewObject ->
            showProgress(viewObject.progress)
            when {
                viewObject.error -> {
                    if (viewObject.throwable != null) {
                        viewObject.throwable.message?.let {
                            WeatherUtil.showToast(
                                requireContext(),
                                it
                            )
                        }
                    } else {
                        WeatherUtil.showToast(requireContext(), getString(R.string.toast_error))
                    }
                }
            }
        })

    }

    private fun initFutureWeatherListView() {
        mAdapter = FutureWeatherAdapter()
        futureWeatherRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
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
        fun newInstance(cityId: Int): FutureWeatherFragment {
            val f = FutureWeatherFragment()
            val fragmentBundle = Bundle()
            cityId.let { fragmentBundle.putInt(DATA_CITY_ID_ARGS, it) }
            f.arguments = fragmentBundle
            return f
        }
    }
}
