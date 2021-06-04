package com.example.miketoide.domain.repository

import android.content.Context
import com.example.miketoide.R
import com.example.miketoide.data.repository.AppSearch
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RepositoryApiRetrofit @Inject constructor(
    @ApplicationContext private val appContext: Context
) : IRepositoryApi
{
    private val appsService: IAppsService by lazy {
        retrofitInstance.create(IAppsService::class.java)
    }

    private val retrofitInstance : Retrofit by lazy {
        val connectionTimeout = appContext.resources.getInteger(R.integer.connection_timeout_seconds).toLong()

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(connectionTimeout, TimeUnit.SECONDS)
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .cache(cacheDetails)
            .build()

        Retrofit.Builder()
            .baseUrl(appContext.getString(R.string.api_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val cacheDetails : Cache by lazy {
        Cache(appContext.cacheDir, appContext.resources.getInteger(R.integer.cache_size).toLong())
    }

    override suspend fun getApps(): AppSearch = appsService.getApps()
}