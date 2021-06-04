package com.example.miketoide.data.repository

import com.google.gson.annotations.SerializedName

data class Datasets(
    @SerializedName("all")
    val appsFullSet : AppSet
)
