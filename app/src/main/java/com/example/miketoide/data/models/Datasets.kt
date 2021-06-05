package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class Datasets(
    @SerializedName("all")
    val appsFullSet : AppSet
)
