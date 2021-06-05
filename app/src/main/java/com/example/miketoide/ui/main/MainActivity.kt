package com.example.miketoide.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.miketoide.data.events.BaseEvent
import com.example.miketoide.data.mappers.EventTypesMapper
import com.example.miketoide.data.types.EventTypes
import com.example.miketoide.databinding.ActivityMainBinding
import com.example.miketoide.domain.alerts.IAlertApi
import com.example.miketoide.domain.animation.IAnimationApi
import com.example.miketoide.domain.events.IEventApi
import com.example.miketoide.domain.image.IImageApi
import com.example.miketoide.domain.scope.ScopeApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
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

    private lateinit var binding: ActivityMainBinding
    private lateinit var allAppsListAdapter: AllAppsListAdapter
    private val vm by viewModels<MainViewModel>()

    private val eventsMapper : Map<EventTypes, (Map<String, Any>?) -> Unit> by lazy {
        mapOf(
            EventTypes.ShowLoading to ::onShowLoading,
            EventTypes.HideLoading to ::onHideLoading,
            EventTypes.ErrorNetwork to ::onNetworkError,
            EventTypes.ErrorGeneric to ::onAlertMessage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        setRefreshAppSearchNotFoundButton()
        setRefreshAppSearchNetworkErrorButton()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState()")
        vm.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState()")
        super.onRestoreInstanceState(savedInstanceState)
        vm.onRestoreInstanceState(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart()")
        super.onStart()
        registerEvents()
        registerObservers()
        vm.onInit()
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()
        unregisterEvents()
        unregisterObservers()
    }

    @Subscribe
    fun onEvent(event : BaseEvent) {
        Log.d(TAG, "onEvent() eventType: ${event.eventType} | payload: ${event.payload}")
        try {
            val eventMapperFunction = eventsMapper[event.eventType] ?: throw NullPointerException("invalid event type")
            eventMapperFunction(event.payload)
        } catch (e : Exception) {
            e.printStackTrace()
            showGenericErrorMessage()
        }
    }

    private fun setRefreshAppSearchNotFoundButton() {
        Log.d(TAG, "setRefreshAppSearchNotFoundButton()")
        binding.refreshAppSearchNotFoundButton.setOnClickListener {
            Log.d(TAG, "setRefreshAppSearchNotFoundButton().onClick()")
            vm.onSearch()
        }
    }

    private fun setRefreshAppSearchNetworkErrorButton() {
        Log.d(TAG, "setRefreshAppSearchNetworkErrorButton()")
        binding.refreshAppSearchNetworkErrorButton.setOnClickListener {
            Log.d(TAG, "setRefreshAppSearchNetworkErrorButton().onClick()")
            vm.onSearch()
        }
    }

    private fun registerEvents() {
        Log.d(TAG, "registerEvents()")
        eventApi.register(this)
    }

    private fun unregisterEvents() {
        Log.d(TAG, "unregisterEvents()")
        eventApi.unregister(this)
    }

    private fun registerObservers() {
        Log.d(TAG, "registerObservers()")
        registerAllAppsObserver()
        registerTopAppsObserver()
    }

    private fun registerAllAppsObserver() {
        Log.d(TAG, "registerAllAppsObserver()")
    }

    private fun registerTopAppsObserver() {
        Log.d(TAG, "registerTopAppsObserver()")
    }

    private fun unregisterObservers() {
        Log.d(TAG, "unregisterObservers()")
        unregisterAllAppsObserver()
        unregisterTopAppsObserver()
    }

    private fun unregisterTopAppsObserver() {
        Log.d(TAG, "unregisterTopAppsObserver()")
    }

    private fun unregisterAllAppsObserver() {
        Log.d(TAG, "unregisterAllAppsObserver()")
    }

    private fun onShowLoading(payload: Map<String, Any>?) {
        Log.d(TAG, "onShowLoading() payload: $payload")
        runOnUiThread {
            animationApi.showLoading()
        }
    }

    private fun onHideLoading(payload: Map<String, Any>?) {
        Log.d(TAG, "onHideLoading() payload: $payload")
        runOnUiThread {
            animationApi.hideLoading()
        }
    }

    private fun onNetworkError(payload: Map<String, Any>?) {
        Log.d(TAG, "onNetworkError() payload: $payload")
        hideMainContainer()
        hideNotFoundContainer()
        showNetworkErrorContainer()
    }

    /*
        Note: It is true that this method centralizes every search error type and
        could have been spread into 'N' (error specific) different methods, to handle each error type.
        But, for simplicity of this exercise, I've put all handlers into one method.
    */
    private fun onAlertMessage(payload: Map<String, Any>?) {
        Log.d(TAG, "onAlertMessage() payload: $payload")
        if (payload == null) {
            throw NullPointerException("payload cannot be null for search errors")
        }
        val message = payload[EventTypesMapper.MESSAGE]?.toString() ?: throw IndexOutOfBoundsException("key not in map")
        showMessage(message)
    }

    private fun showGenericErrorMessage() = ScopeApi.main().launch {
        Log.d(TAG, "showGenericErrorMessage()")
        alertApi.showGenericErrorMessage(this@MainActivity)
    }

    private fun showMessage(message : String) = ScopeApi.main().launch {
        Log.d(TAG, "showMessage() message: $message")
        alertApi.showMessage(this@MainActivity, message)
    }

    private fun showMainContainer() = ScopeApi.main().launch {
        Log.d(TAG, "showMainContainer()")
        binding.containerMain.visibility = View.VISIBLE
    }

    private fun hideMainContainer() = ScopeApi.main().launch {
        Log.d(TAG, "hideMainContainer()")
        binding.containerMain.visibility = View.GONE
    }

    private fun showNotFoundContainer() = ScopeApi.main().launch {
        Log.d(TAG, "showNotFoundContainer()")
        binding.containerNotFound.visibility = View.VISIBLE
    }

    private fun hideNotFoundContainer() = ScopeApi.main().launch {
        Log.d(TAG, "hideNotFoundContainer()")
        binding.containerNotFound.visibility = View.GONE
    }

    private fun showNetworkErrorContainer() = ScopeApi.main().launch {
        Log.d(TAG, "showNetworkErrorContainer()")
        binding.containerNetworkError.visibility = View.VISIBLE
    }

    private fun hideNetworkErrorContainer() = ScopeApi.main().launch {
        Log.d(TAG, "hideNetworkErrorContainer()")
        binding.containerNetworkError.visibility = View.GONE
    }
}