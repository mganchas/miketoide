package com.example.miketoide.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miketoide.data.extensions.roundToOneDecimal
import com.example.miketoide.data.extensions.toHumanDownloadSizeFormat
import com.example.miketoide.data.extensions.toHumanDownloadedTimesFormat
import com.example.miketoide.data.models.AppData
import com.example.miketoide.databinding.AppsItemBinding
import com.example.miketoide.domain.image.IImageApi
import com.example.miketoide.domain.scope.ScopeApi
import kotlinx.coroutines.launch

class AllAppsListAdapter(
    context: Context,
    private val imageApi : IImageApi
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var apps = mutableListOf<AppData>()

    inner class AppsViewHolder(private val binding : AppsItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(app: AppData)
        {
            binding.apply {
                appName.text = app.name
                appDownloads.text = app.downloads.toHumanDownloadedTimesFormat()
                appSize.text = app.size.toHumanDownloadSizeFormat()
                appRating.text = app.rating.roundToOneDecimal().toString()
                imageApi.loadImageFromUrlIntoView(app.icon, appIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = AppsItemBinding.inflate(inflater, parent, false)
        return AppsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AppsViewHolder).bind(apps[position])
    }

    override fun getItemCount() = apps.size

    fun clearData() {
        apps.clear()
        updateView()
    }

    fun setData(appsList: List<AppData>) {
        // to prevent racing conditions
        synchronized(this) {
            clearData()
            apps.addAll(appsList)
            updateView()
        }
    }

    private fun updateView() = ScopeApi.main().launch {
        notifyDataSetChanged()
    }
}