package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class Responses(
    @SerializedName("listApps")
    val listApps : ListApps
)
