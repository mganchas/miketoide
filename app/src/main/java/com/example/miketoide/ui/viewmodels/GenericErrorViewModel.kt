package com.example.miketoide.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.miketoide.data.events.GetAppsEvent
import com.example.miketoide.domain.events.IEventApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenericErrorViewModel @Inject constructor(
    private val eventApi: IEventApi
) : ViewModel()
{
    companion object {
        private val TAG = GenericErrorViewModel::class.java.simpleName
    }

    fun onTryAgain() {
        Log.d(TAG, "onTryAgain()")
        sendEventGetApps()
    }

    private fun sendEventGetApps() {
        Log.d(TAG, "sendEventGetApps()")
        eventApi.publish(GetAppsEvent())
    }
}