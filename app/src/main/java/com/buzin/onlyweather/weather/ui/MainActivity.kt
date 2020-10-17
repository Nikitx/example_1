package com.buzin.onlyweather.weather.ui

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.buzin.onlyweather.R
import com.buzin.onlyweather.extensions.replaceFragmentInActivity
import com.buzin.onlyweather.util.Constants.Companion.TAG_ADD_CITY_DIALOG
import com.buzin.onlyweather.util.WeatherUtil
import com.buzin.onlyweather.weather.dialog.AddCityDialog
import com.buzin.onlyweather.weather.ui.list_weather.ListWeatherFragment
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity(), AddCityDialog.AddCityDialogListener {

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            AddCityDialog.newInstance(this).show(supportFragmentManager, TAG_ADD_CITY_DIALOG)
        }
        showListFragment()
    }

    override fun onDialogPositiveClick(dialog: DaggerDialogFragment?) {
      //  WeatherUtil.showToast(this, getString(R.string.toast_add_new_city))
    }

    override fun onDialogNegativeClick(dialog: DaggerDialogFragment?) {
    }

    private fun showListFragment() {
        val listFragment = ListWeatherFragment()
        replaceFragmentInActivity(listFragment)
    }
}
