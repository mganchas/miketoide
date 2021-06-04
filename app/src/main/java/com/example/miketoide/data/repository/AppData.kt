package com.example.miketoide.data.repository

import com.google.gson.annotations.SerializedName

data class AppData(
    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name: String,

    @SerializedName("vername")
    val version: String,

    @SerializedName("downloads")
    val downloads: Int,

    @SerializedName("added")
    val launchDate: String,

    @SerializedName("ratind")
    val rating: Double,

    @SerializedName("icon")
    val icon: String,
)