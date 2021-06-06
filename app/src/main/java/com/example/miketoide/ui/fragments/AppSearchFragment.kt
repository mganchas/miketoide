package com.example.miketoide.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miketoide.R
import com.example.miketoide.data.models.AppData
import com.example.miketoide.databinding.FragmentAppSearchBinding
import com.example.miketoide.domain.alerts.IAlertApi
import com.example.miketoide.domain.image.IImageApi
import com.example.miketoide.ui.adapters.AllAppsListAdapter
import com.example.miketoide.ui.adapters.TopAppsListAdapter
import com.example.miketoide.ui.viewmodels.AppSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppSearchFragment : Fragment(R.layout.fragment_app_search)
{
    @Inject
    lateinit var alertApi : IAlertApi

    @Inject
    lateinit var imageApi : IImageApi

    companion object {
        private val TAG = AppSearchFragment::class.java.simpleName
    }

    private val vm by viewModels<AppSearchViewModel>()

    private var _binding: FragmentAppSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var allAppsListAdapter: AllAppsListAdapter
    private lateinit var topAppsListAdapter: TopAppsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        setAllAppsRecyclerView { setAllAppsListAdapter() }
        setTopAppsRecyclerView { setTopAppsListAdapter() }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getApps()
    }

    override fun onResume() {
        super.onResume()
        registerObservers()
    }

    override fun onPause() {
        unregisterObservers()
        super.onPause()
    }

    private fun setAllAppsListAdapter() {
        Log.d(TAG, "setAllAppsListAdapter()")
        allAppsListAdapter = AllAppsListAdapter(this.requireContext(), imageApi)
    }

    private fun setTopAppsListAdapter() {
        Log.d(TAG, "setTopAppsListAdapter()")
        topAppsListAdapter = TopAppsListAdapter(this.requireContext(), imageApi)
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
            layoutManager = LinearLayoutManager(this@AppSearchFragment.requireContext())
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
            layoutManager = LinearLayoutManager(this@AppSearchFragment.requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun getApps() {
        Log.d(TAG, "getApps()")
        vm.onSearch()
    }

    private fun registerObservers() {
        Log.d(TAG, "registerObservers()")
        registerAllAppsObserver()
        registerTopAppsObserver()
    }

    private fun unregisterObservers() {
        Log.d(TAG, "unregisterObservers()")
        unregisterAllAppsObserver()
        unregisterTopAppsObserver()
    }

    private fun registerAllAppsObserver() {
        Log.d(TAG, "registerAllAppsObserver()")
        vm.allApps.observe(this, {
            Log.d(TAG, "registerAllAppsObserver().onObserve() value: $it")
            when(it) {
                null -> clearAllAppsSearch()
                else -> updateAllAppsSearch(it)
            }
        })
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

    private fun unregisterAllAppsObserver() {
        Log.d(TAG, "unregisterAllAppsObserver()")
        vm.allApps.removeObservers(this)
    }

    private fun unregisterTopAppsObserver() {
        Log.d(TAG, "unregisterTopAppsObserver()")
        vm.topApps.removeObservers(this)
    }

    private fun clearAllAppsSearch() {
        Log.d(TAG, "clearAllAppsSearch()")
        allAppsListAdapter.clearData()
    }

    private fun updateAllAppsSearch(value : List<AppData>) {
        Log.d(TAG, "updateAllAppsSearch() value.size: ${value.size}")
        allAppsListAdapter.setData(value)
    }

    private fun clearTopAppsSearch() {
        Log.d(TAG, "clearTopAppsSearch()")
        topAppsListAdapter.clearData()
    }

    private fun updateTopAppsSearch(value : List<AppData>) {
        Log.d(TAG, "updateTopAppsSearch() value.size: ${value.size}")
        topAppsListAdapter.setData(value)
    }
}