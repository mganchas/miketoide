package com.example.miketoide.domain.repository

import com.example.miketoide.data.repository.AppSearch

interface IRepositoryApi {
    suspend fun getApps() : AppSearch
}