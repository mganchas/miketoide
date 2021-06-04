package com.example.miketoide.data.repository

import com.google.gson.annotations.SerializedName

data class AppData(
    @SerializedName("id")
    val id : Long,

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

    @SerializedName("ratind")
    val rating: Double,

    @SerializedName("icon")
    val icon: String,
)
