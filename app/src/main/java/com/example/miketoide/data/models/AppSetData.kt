package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class AppSetData(
    @SerializedName("total")
    val total: Int,

    @SerializedName("list")
    val apps: List<AppData>,
)
