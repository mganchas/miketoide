package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class ListApps(
    @SerializedName("info")
    val info: Info,

    @SerializedName("datasets")
    val datasets : Datasets
)
