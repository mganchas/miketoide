package com.example.miketoide.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.miketoide.R
import com.example.miketoide.domain.alerts.IAlertApi
import com.example.miketoide.domain.animation.IAnimationApi
import com.example.miketoide.domain.image.IImageApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    @Inject
    lateinit var alertApi : IAlertApi

    @Inject
    lateinit var loadingApi : IAnimationApi

    @Inject
    lateinit var imageApi : IImageApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}