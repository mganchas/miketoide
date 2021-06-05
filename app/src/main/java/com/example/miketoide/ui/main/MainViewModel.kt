package com.example.miketoide.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miketoide.R
import com.example.miketoide.data.events.BaseEvent
import com.example.miketoide.data.mappers.EventTypesMapper
import com.example.miketoide.data.repository.AppData
import com.example.miketoide.data.repository.AppSearch
import com.example.miketoide.data.types.EventTypes
import com.example.miketoide.domain.connectivity.ConnectivityApi
import com.example.miketoide.domain.events.IEventApi
import com.example.miketoide.domain.repository.IRepositoryApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val eventApi: IEventApi,
    private val repositoryApi: IRepositoryApi,
    private val connectivityApi: ConnectivityApi
) : ViewModel()
{
    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        private const val KEY_TOP_APPS = "topApps"
        private const val KEY_ALL_APPS = "allApps"
    }

    val allApps: MutableLiveData<List<AppData>?> by lazy {
        MutableLiveData()
    }
    val appsCount : MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }
    val topApps: MutableLiveData<List<AppData>?> by lazy {
        MutableLiveData()
    }

    private var appSearch : AppSearch? = null

    private val alertMessageGeneric : String by lazy {
        context.getString(R.string.alert_generic_error)
    }

    fun onInit() {
        Log.d(TAG, "onInit()")
        onSearch()
    }

    fun onSearch()
    {
        Log.d(TAG, "onSearch()")
        sendEventShowLoading()
        try {
            clearResults()
            val hasInternet = connectivityApi.hasInternet()
            Log.d(TAG, "onSearch() hasInternet: $hasInternet")
            if (!hasInternet) {
                sendEventErrorNetwork()
                return
            }

        } catch (e: Exception) {
            e.printStackTrace()
            handleExceptions(e)
        }
        finally {
            sendEventHideLoading()
        }
    }

    fun onSaveInstanceState(bundle: Bundle) {
        Log.d(TAG, "onSaveInstanceState()")
        saveAllAppsState(bundle)
        saveTopAppsState(bundle)
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        Log.d(TAG, "onRestoreInstanceState()")
        getAllAppsState(bundle)
        getTopAppsState(bundle)
    }

    private fun clearResults() = viewModelScope.launch {
        Log.d(TAG, "clearResults()")
        setAppSearch(null)
        setAppsCount(0)
    }

    private fun setAppsCount(value: Int)  {
        Log.d(TAG, "setAppsCount() value: $value")
        appsCount.value = value
    }

    private fun setAppSearch(value: AppSearch?)  {
        Log.d(TAG, "setPokemonSearch() value: $value")
        appSearch = value
    }

    private fun handleExceptions(e: Exception) {
        Log.e(TAG, "handleExceptions() cause: ${e.cause} | message: ${e.message}")
        when (e) {
            is SocketTimeoutException, is HttpException -> {
                sendEventErrorNetwork()
            }
            else -> {
                sendEventErrorGeneric()
            }
        }
    }

    private fun saveTopAppsState(bundle: Bundle) {
        Log.d(TAG, "saveTopAppsState()")
    }

    private fun saveAllAppsState(bundle: Bundle) {
        Log.d(TAG, "saveAllAppsState()")
    }

    private fun getAllAppsState(bundle: Bundle) {
        Log.d(TAG, "getAllAppsState()")
    }

    private fun getTopAppsState(bundle: Bundle) {
        Log.d(TAG, "getTopAppsState()")
    }

    private fun sendEventErrorGeneric() {
        Log.d(TAG, "sendEventErrorGeneric()")
        val event = BaseEvent(
            EventTypes.ErrorGeneric,
            mapOf(EventTypesMapper.MESSAGE to alertMessageGeneric)
        )
        eventApi.publish(event)
    }

    private fun sendEventErrorNetwork() {
        Log.d(TAG, "sendEventErrorNetwork()")
        val event = BaseEvent(EventTypes.ErrorNetwork)
        eventApi.publish(event)
    }

    private fun sendEventShowLoading() {
        Log.d(TAG, "sendEventShowLoading()")
        val event = BaseEvent(EventTypes.ShowLoading)
        eventApi.publish(event)
    }

    private fun sendEventHideLoading() {
        Log.d(TAG, "sendEventHideLoading()")
        val event = BaseEvent(EventTypes.HideLoading)
        eventApi.publish(event)
    }
}