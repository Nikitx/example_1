package com.buzin.onlyweather.util

import android.content.SharedPreferences
import com.buzin.onlyweather.WeatherApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySettings @Inject constructor(
    private var weatherApplication: WeatherApplication
) {


    private val PREF_NAME = "my-settings"

    private var PRIVATE_MODE = 0

    private val NEED_TO_ADD_DEMO = "need_to_add_demo"
    private val KEY_LAST_SYNC = "last_sync"

    private var mPref: SharedPreferences
    private var mEditor: SharedPreferences.Editor

    init {
        mPref = weatherApplication.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        mEditor = mPref.edit()

    }



    fun setNeedToAddDemoCities(needDemo: Boolean) {
        mEditor.putBoolean(NEED_TO_ADD_DEMO, needDemo)
        mEditor.commit()
    }

    fun needToAddDemoCities(): Boolean {
        return mPref.getBoolean(NEED_TO_ADD_DEMO, true)
    }


    fun lastSync(date: Long) {
        mEditor.putLong(KEY_LAST_SYNC, date)
        mEditor.commit()
    }

    fun lastSync(): Long {
        return mPref.getLong(KEY_LAST_SYNC, -1)
    }

    companion object {


    }
}