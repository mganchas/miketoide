package com.example.miketoide.domain.repository

import com.example.miketoide.data.models.AppSearch

interface IRepositoryApi {
    suspend fun getApps() : AppSearch
}