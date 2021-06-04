package com.example.miketoide.domain.persistence

import com.example.miketoide.data.persistence.App
import io.objectbox.Box

interface IPersistenceApi {
    fun init()
    fun clean()

    // list of available DAOs
    fun appDAO() : Box<App>
}