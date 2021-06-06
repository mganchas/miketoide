package com.example.miketoide.domain.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectivityApi @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : IConnectivityApi
{
    companion object {
        private val TAG = ConnectivityApi::class.java.simpleName
    }

    override fun hasInternet() : Boolean {
        Log.d(TAG, "hasInternet()")
        val connMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}