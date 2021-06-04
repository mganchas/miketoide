package com.example.miketoide.domain.persistence

import android.content.Context
import com.example.miketoide.data.persistence.App
import com.example.miketoide.data.MyObjectBox
import dagger.hilt.android.qualifiers.ApplicationContext
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import javax.inject.Inject

class PersistenceApiObjectBox  @Inject constructor(
    @ApplicationContext private val appContext: Context
) : IPersistenceApi
{
    private lateinit var boxStore: BoxStore

    private val appDao : Box<App> by lazy {
        boxStore.boxFor()
    }

    override fun init() {
        boxStore = MyObjectBox.builder()
            .androidContext(appContext.applicationContext)
            .build()
    }

    /*
        Remove entries from every app DAO to make a full database cleanup
     */
    override fun clean() {
        appDao.removeAll()
    }

    override fun appDAO(): Box<App> = appDao
}