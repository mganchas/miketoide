package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class AppSet(
    @SerializedName("info")
    val info: Info,

    @SerializedName("data")
    val data: AppSetData
)
