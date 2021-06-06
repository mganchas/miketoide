package com.example.miketoide.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miketoide.data.extensions.roundToOneDecimal
import com.example.miketoide.data.models.AppData
import com.example.miketoide.databinding.AppsTopItemBinding
import com.example.miketoide.domain.image.IImageApi
import com.example.miketoide.domain.scope.ScopeApi
import kotlinx.coroutines.launch

class TopAppsListAdapter(
    context: Context,
    private val imageApi : IImageApi
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var apps = mutableListOf<AppData>()

    inner class AppsViewHolder(private val binding : AppsTopItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(app: AppData)
        {
            binding.apply {
                appName.text = app.name
                appRating.text = app.rating.roundToOneDecimal().toString()
                imageApi.loadImageFromUrlIntoView(app.icon, appIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val binding = AppsTopItemBinding.inflate(inflater, parent, false)
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