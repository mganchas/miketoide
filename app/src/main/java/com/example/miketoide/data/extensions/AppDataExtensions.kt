package com.example.miketoide.data.extensions

import com.example.miketoide.data.persistence.App
import com.example.miketoide.data.repository.AppData

fun AppData.toApp() : App {
    return App(
        id = this.id,
        name = this.name,
        downloads = this.downloads,
        version = this.version,
        launchDate = this.launchDate,
        rating = this.rating,
        icon = this.icon
    )
}