package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class AppData(
    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name: String,

    @SerializedName("vername")
    val version: String,

    @SerializedName("size")
    val size: Long,

    @SerializedName("downloads")
    val downloads: Long,

    @SerializedName("added")
    val launchDate: String,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("icon")
    val icon: String,
)
