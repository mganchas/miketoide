package com.example.miketoide.domain.persistence

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PersistenceApiModule {
    @Provides
    fun providePersistenceApi(@ApplicationContext appContext: Context) : IPersistenceApi = PersistenceApiObjectBox(appContext)
}