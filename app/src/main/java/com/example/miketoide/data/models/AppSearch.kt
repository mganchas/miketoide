package com.example.miketoide.data.models

import com.google.gson.annotations.SerializedName

data class AppSearch(
    @SerializedName("status")
    val status : String,

    @SerializedName("responses")
    val responses : Responses
)
