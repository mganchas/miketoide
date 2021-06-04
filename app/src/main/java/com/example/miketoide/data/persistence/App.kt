package com.example.miketoide.data.persistence

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

@Entity
data class App(
    @Id var _id : Long? = null,
    val id : String,
    val name: String,
    val version: String,
    val downloads: Int,
    val launchDate: String,
    val rating: Double,
    val icon: String,
) : Serializable
