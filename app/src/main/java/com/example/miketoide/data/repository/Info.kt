package com.example.miketoide.data.repository

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("status")
    val status: String,

    @SerializedName("time")
    val time: InfoTime
)
