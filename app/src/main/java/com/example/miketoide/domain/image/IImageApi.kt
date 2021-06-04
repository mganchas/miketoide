package com.example.miketoide.domain.image

import android.widget.ImageView

interface IImageApi {
    fun loadImageFromUrlIntoView(url: String, view: ImageView)
}