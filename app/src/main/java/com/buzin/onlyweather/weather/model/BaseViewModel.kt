package com.buzin.onlyweather.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

data class ViewObject<T>(
    var progress: Boolean,
    val error: Boolean,
    val throwable: Throwable?
)

abstract class BaseViewModel<T> : ViewModel() {

     val mutableLiveData = MutableLiveData<ViewObject<T>>().apply {
        value = ViewObject(
            progress = false,
            error = false,
            throwable = null
        )
    }

    private val observer: Observer<in T> = Observer {
        mutableLiveData.postValue(
            currentData?.copy(
                progress = false,
                error = false,
                throwable = null
            )
        )
    }

    private val currentData = mutableLiveData.value

    val scope = CoroutineScope(Dispatchers.Main)

    protected abstract fun createLiveData(): LiveData<T>?

    fun liveData(): LiveData<ViewObject<T>> = mutableLiveData

    abstract fun refreshData()

    protected fun fetchData() {
        val currentData = mutableLiveData.value
        if (currentData?.progress == true) {
            return
        }
        mutableLiveData.postValue(currentData?.copy(progress = true))

        if (createLiveData() != null) {
            createLiveData()?.observeForever(observer)
        } else {
            mutableLiveData.postValue(
                currentData?.copy(
                    progress = false,
                    error = true,
                    throwable = Throwable(message = "No data found")
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        createLiveData()?.removeObserver(observer)
    }
}