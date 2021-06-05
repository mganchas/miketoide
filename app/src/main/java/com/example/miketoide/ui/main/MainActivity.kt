package com.example.miketoide.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miketoide.data.events.BaseEvent
import com.example.miketoide.data.models.AppData
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
    private lateinit var topAppsListAdapter: TopAppsListAdapter
    private val vm by viewModels<MainViewModel>()

    private val eventsMapper : Map<EventTypes, (Map<String, Any>?) -> Unit> by lazy {
        mapOf(
            EventTypes.ShowLoading to ::onShowLoading,
            EventTypes.HideLoading to ::onHideLoading,
            EventTypes.ErrorNetwork to ::onNetworkError,
            EventTypes.ErrorGeneric to ::onGenericError
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
        setAllAppsRecyclerView { setAllAppsListAdapter() }
        setTopAppsRecyclerView { setTopAppsListAdapter() }
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

    private fun setAllAppsListAdapter() {
        Log.d(TAG, "setAllAppsListAdapter()")
        allAppsListAdapter = AllAppsListAdapter(this, imageApi)
    }

    private fun setTopAppsListAdapter() {
        Log.d(TAG, "setTopAppsListAdapter()")
        topAppsListAdapter = TopAppsListAdapter(this, imageApi)
    }

    /*
        Note: adding some temporal coupling (recyclerView needs the adapter set up prior to
        its own setting)
    */
    private fun setAllAppsRecyclerView(preFunction : () -> Unit) {
        Log.d(TAG, "setAllAppsRecyclerView()")
        preFunction()
        binding.allAppsRecyclerView.apply {
            adapter = allAppsListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    /*
        Note: adding some temporal coupling (recyclerView needs the adapter set up prior to
        its own setting)
    */
    private fun setTopAppsRecyclerView(preFunction : () -> Unit) {
        Log.d(TAG, "setTopAppsRecyclerView()")
        preFunction()
        binding.topAppsRecyclerView.apply {
            adapter = topAppsListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
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
        vm.allApps.observe(this, {
            Log.d(TAG, "registerAllAppsObserver().onObserve() value: $it")
            hideNotFoundContainer()
            hideNetworkErrorContainer()
            showMainContainer()

            when(it) {
                null -> clearAllAppsSearch()
                else -> updateAllAppsSearch(it)
            }
        })
    }

    private fun clearAllAppsSearch() {
        Log.d(TAG, "clearAllAppsSearch()")
        allAppsListAdapter.clearData()
    }

    private fun updateAllAppsSearch(value : List<AppData>) {
        Log.d(TAG, "updateAllAppsSearch() value.size: ${value.size}")
        allAppsListAdapter.setData(value)
    }

    private fun registerTopAppsObserver() {
        Log.d(TAG, "registerTopAppsObserver()")
        vm.topApps.observe(this, {
            Log.d(TAG, "registerTopAppsObserver().onObserve() value: $it")
            when(it) {
                null -> clearTopAppsSearch()
                else -> updateTopAppsSearch(it)
            }
        })
    }

    private fun clearTopAppsSearch() {
        Log.d(TAG, "clearTopAppsSearch()")
        topAppsListAdapter.clearData()
    }

    private fun updateTopAppsSearch(value : List<AppData>) {
        Log.d(TAG, "updateTopAppsSearch() value.size: ${value.size}")
        topAppsListAdapter.setData(value)
    }

    private fun unregisterObservers() {
        Log.d(TAG, "unregisterObservers()")
        unregisterAllAppsObserver()
        unregisterTopAppsObserver()
    }

    private fun unregisterAllAppsObserver() {
        Log.d(TAG, "unregisterAllAppsObserver()")
        vm.allApps.removeObservers(this)
    }

    private fun unregisterTopAppsObserver() {
        Log.d(TAG, "unregisterTopAppsObserver()")
        vm.topApps.removeObservers(this)
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

    private fun onGenericError(payload: Map<String, Any>?) {
        Log.d(TAG, "onGenericError() payload: $payload")
        hideMainContainer()
        hideNetworkErrorContainer()
        showNotFoundContainer()
        showGenericErrorMessage()
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