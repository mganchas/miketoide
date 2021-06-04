package com.example.miketoide.domain.repository

import com.example.miketoide.data.repository.AppSearch
import retrofit2.http.GET

interface IAppsService {
    @GET("listApps")
    suspend fun getApps() : AppSearch
}