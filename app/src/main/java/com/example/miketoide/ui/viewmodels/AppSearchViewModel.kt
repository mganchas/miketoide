package com.example.miketoide.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miketoide.data.events.*
import com.example.miketoide.data.models.AppData
import com.example.miketoide.data.models.AppSearch
import com.example.miketoide.domain.connectivity.ConnectivityApi
import com.example.miketoide.domain.events.IEventApi
import com.example.miketoide.domain.repository.IRepositoryApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AppSearchViewModel @Inject constructor(
    private val eventApi: IEventApi,
    private val repositoryApi: IRepositoryApi,
    private val connectivityApi: ConnectivityApi
) : ViewModel()
{
    companion object {
        private val TAG = AppSearchViewModel::class.java.simpleName
    }

    val allApps: MutableLiveData<List<AppData>?> by lazy {
        MutableLiveData()
    }
    val topApps: MutableLiveData<List<AppData>?> by lazy {
        MutableLiveData()
    }
    private var appSearch : AppSearch? = null
    
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

    private fun clearResults() = viewModelScope.launch {
        Log.d(TAG, "clearResults()")
        setAppSearch(null)
        setAllApps(null)
        setTopApps(null)
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

    private fun sendEventErrorGeneric() {
        Log.d(TAG, "sendEventErrorGeneric()")
        eventApi.publish(ErrorGenericEvent())
    }

    private fun sendEventErrorNetwork() {
        Log.d(TAG, "sendEventErrorNetwork()")
        eventApi.publish(ErrorNetworkEvent())
    }

    private fun sendEventShowLoading() {
        Log.d(TAG, "sendEventShowLoading()")
        eventApi.publish(ShowLoadingEvent())
    }

    private fun sendEventHideLoading() {
        Log.d(TAG, "sendEventHideLoading()")
        eventApi.publish(HideLoadingEvent())
    }
}