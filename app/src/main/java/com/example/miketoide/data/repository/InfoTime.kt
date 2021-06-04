package com.example.miketoide.data.repository

import com.google.gson.annotations.SerializedName

data class InfoTime(
    @SerializedName("seconds")
    val seconds: Float,

    @SerializedName("human")
    val humanTime: String
)
