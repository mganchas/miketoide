package com.example.miketoide.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miketoide.R
import com.example.miketoide.data.events.BaseEvent
import com.example.miketoide.data.models.AppData
import com.example.miketoide.data.models.AppSearch
import com.example.miketoide.data.types.EventTypes
import com.example.miketoide.domain.connectivity.ConnectivityApi
import com.example.miketoide.domain.events.IEventApi
import com.example.miketoide.domain.repository.IRepositoryApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun onSearch() = viewModelScope.launch {
        Log.d(TAG, "onSearch()")
        sendEventShowLoading()
        try {
            clearResults()

            val hasInternet = connectivityApi.hasInternet()
            Log.d(TAG, "onSearch() hasInternet: $hasInternet")
            if (!hasInternet) {
                sendEventErrorNetwork()
                return@launch
            }

            val searchResults = getApps()
            setAppSearch(searchResults)
            setAppsCount(searchResults.responses.listApps.datasets.appsFullSet.data.total)
            setAllApps(searchResults.responses.listApps.datasets.appsFullSet.data.apps)
            generateRandomTopApps()
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
        setAllApps(null)
        setTopApps(null)
    }

    private fun setAppsCount(value: Int)  {
        Log.d(TAG, "setAppsCount() value: $value")
        appsCount.value = value
    }

    private fun setAppSearch(value: AppSearch?)  {
        Log.d(TAG, "setAppSearch() value: $value")
        appSearch = value
    }

    private fun setAllApps(value: List<AppData>?)  {
        Log.d(TAG, "setAllApps() value: $value")
        allApps.value = value
    }

    private fun setTopApps(value: List<AppData>?)  {
        Log.d(TAG, "setTopApps() value: $value")
        topApps.value = value
    }

    private suspend fun getApps() : AppSearch {
        Log.d(TAG, "getAppsSearch()")
        return withContext(Dispatchers.IO) {
            repositoryApi.getApps()
        }
    }

    private fun generateRandomTopApps() {
        Log.d(TAG, "generateRandomTopApps()")
        val allAppsList = allApps.value ?: return
        val topAppsList = allAppsList.sortedByDescending { it.rating }.take(5)
        setTopApps(topAppsList)
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
        val event = BaseEvent(EventTypes.ErrorGeneric)
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