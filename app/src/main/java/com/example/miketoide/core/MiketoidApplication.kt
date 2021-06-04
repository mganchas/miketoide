package com.example.miketoide.core

import android.app.Application
import com.example.miketoide.domain.persistence.IPersistenceApi
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MiketoidApplication @Inject constructor(
    private val persistenceApi: IPersistenceApi,
) : Application()
{
    override fun onCreate() {
        super.onCreate()
        persistenceApi.init()
    }
}