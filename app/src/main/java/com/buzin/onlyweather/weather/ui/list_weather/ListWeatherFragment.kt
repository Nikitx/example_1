package com.buzin.onlyweather.weather.ui.list_weather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzin.onlyweather.R
import androidx.lifecycle.Observer
import com.buzin.onlyweather.extensions.replaceFragment
import com.buzin.onlyweather.extensions.setupTitle
import com.buzin.onlyweather.extensions.viewModelProvider
import com.buzin.onlyweather.util.WeatherUtil
import com.buzin.onlyweather.weather.model.ListWeatherViewModel
import com.buzin.onlyweather.weather.ui.detail_weather.DetailedWeatherFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_list.swipeToRefresh
import kotlinx.android.synthetic.main.fragment_list.futureWeatherRv
import kotlinx.android.synthetic.main.fragment_list.loadingGroup
import javax.inject.Inject

class ListWeatherFragment : DaggerFragment() {
    private val tickReceiver by lazy { makeBroadcastReceiver() }
    private var lastUpdate: Long? = null
    //private lateinit var mSettings: MySettings

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListWeatherViewModel
    private lateinit var listWeatherAdapter: ListWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
//                    mSettings = MySettings.getInstance(requireContext())!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Подписались на БД получаем данные после обновлений
        viewModel.allCities.observe(viewLifecycleOwner, Observer { contacts ->
            contacts.let { listWeatherAdapter.setCitiesList(it) }
        })

        // Записываем данные об обновлении
        viewModel.isDataRefreshed.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.refreshedData()
                lastUpdate = System.currentTimeMillis()
            }
        })

        // Более 24 часов не было обновления
        viewModel.needToastAboutRefresh.observe(viewLifecycleOwner, Observer {
            if (it) WeatherUtil.showToast(
                requireContext(),
                getString(R.string.toast_forecast_outdated)
            )
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListWeatherRecyclerView()

        setupTitle(getString(R.string.list_weather))

        viewModel.liveData().observe(viewLifecycleOwner, Observer { viewObject ->
            showProgress(viewObject.progress)
            when {
                viewObject.error -> {
                    if (viewObject.throwable != null) {
                        viewObject.throwable.message?.let { WeatherUtil.showToast(requireContext(),it) }
                    } else {
                        WeatherUtil.showToast(requireContext(),getString(R.string.toast_error))
                    }
                }
                //viewObject.data == null -> return@Observer
                //else -> showCurrentWeather(viewObject.data as ArrayList<CurrentWeatherModel>?)
            }
        })

        swipeToRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        try {
            requireActivity().unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            WeatherUtil.toLog(e)
        }
        super.onPause()
    }

    private fun initListWeatherRecyclerView() {

        listWeatherAdapter = ListWeatherAdapter { cityId ->
            showDetailedWeather(cityId)
        }

        futureWeatherRv.apply {
            adapter = listWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showDetailedWeather(cityId: Int) {
        val f = DetailedWeatherFragment.newInstance(cityId)
        replaceFragment(f)
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
            swipeToRefresh.isRefreshing = false
        }
    }


    /** С помощью этой штуки будем понимать устарели ли данные 5минут*/
    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    WeatherUtil.toLog("Tick-tak")
                    if (lastUpdate != null) {
                        val now = System.currentTimeMillis()
                        val diffInMin = (now - lastUpdate!!) / 1000 / 60
                        if (diffInMin.toInt() == 5) {
                            WeatherUtil.showToast(
                                requireContext(),
                                getString(R.string.toast_refresh_page)
                            )
                            WeatherUtil.toLog("Tick-tak diffInMin.toInt() == 5")
                        }

                    }
                }
            }
        }
    }


}