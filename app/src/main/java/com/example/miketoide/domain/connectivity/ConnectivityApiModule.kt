package com.example.miketoide.domain.connectivity

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityApiModule {
    @Provides
    fun provideConnectivityApi(@ApplicationContext applicationContext: Context) : IConnectivityApi = ConnectivityApi(applicationContext)
}