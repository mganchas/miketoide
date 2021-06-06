package com.example.miketoide.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.miketoide.R
import com.example.miketoide.data.events.*
import com.example.miketoide.domain.alerts.IAlertApi
import com.example.miketoide.domain.animation.IAnimationApi
import com.example.miketoide.domain.events.IEventApi
import com.example.miketoide.domain.image.IImageApi
import com.example.miketoide.domain.scope.ScopeApi
import com.example.miketoide.ui.fragments.AppSearchFragment
import com.example.miketoide.ui.fragments.GenericErrorFragment
import com.example.miketoide.ui.fragments.NetworkErrorFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main)
{
    @Inject
    lateinit var alertApi : IAlertApi

    @Inject
    lateinit var eventApi : IEventApi

    @Inject
    lateinit var animationApi : IAnimationApi

    @Inject
    lateinit var imageApi : IImageApi

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            showAppSearch()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        registerEvents()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        unregisterEvents()
        super.onStop()
    }

    @Subscribe
    fun onEventShowLoading(event : ShowLoadingEvent) {
        Log.d(TAG, "onEventShowLoading()")
        onShowLoading()
    }

    @Subscribe
    fun onEventHideLoading(event : HideLoadingEvent) {
        Log.d(TAG, "onEventHideLoading()")
        onHideLoading()
    }

    @Subscribe
    fun onEventErrorGeneric(event: ErrorGenericEvent) {
        Log.d(TAG, "onEventErrorGeneric()")
        onGenericError()
    }

    @Subscribe
    fun onEventErrorNetwork(event: ErrorNetworkEvent) {
        Log.d(TAG, "onEventErrorNetwork()")
        onNetworkError()
    }

    @Subscribe
    fun onEventGetApps(event: GetAppsEvent) {
        Log.d(TAG, "onEventGetApps()")
        onGetApps()
    }

    private fun registerEvents() {
        Log.d(TAG, "registerEvents()")
        eventApi.register(this)
    }

    private fun unregisterEvents() {
        Log.d(TAG, "unregisterEvents()")
        eventApi.unregister(this)
    }

    private fun onGetApps() {
        Log.d(TAG, "onGetApps()")
        showAppSearch()
    }

    private fun onShowLoading() = ScopeApi.main().launch {
        Log.d(TAG, "onShowLoading()")
        animationApi.showLoading()
    }

    private fun onHideLoading() = ScopeApi.main().launch {
        Log.d(TAG, "onHideLoading()")
        animationApi.hideLoading()
    }

    private fun onGenericError() = ScopeApi.main().launch {
        Log.d(TAG, "onGenericError()")
        showGenericErrorMessage()
        showGenericError()
    }

    private fun onNetworkError() = ScopeApi.main().launch {
        Log.d(TAG, "onNetworkError()")
        showNetworkError()
    }

    private fun showGenericErrorMessage() = ScopeApi.main().launch {
        Log.d(TAG, "showGenericErrorMessage()")
        alertApi.showGenericErrorMessage(this@MainActivity)
    }

    private fun showAppSearch() = ScopeApi.main().launch {
        Log.d(TAG, "showAppSearch()")
        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace<AppSearchFragment>(R.id.fragment_container_view)
        }
    }

    private fun showGenericError() = ScopeApi.main().launch {
        Log.d(TAG, "showGenericError()")
        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace<GenericErrorFragment>(R.id.fragment_container_view)
        }
    }

    private fun showNetworkError() = ScopeApi.main().launch {
        Log.d(TAG, "showNetworkError()")
        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace<NetworkErrorFragment>(R.id.fragment_container_view)
        }
    }
}