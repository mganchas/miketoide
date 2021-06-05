package com.example.miketoide.data.events

import com.example.miketoide.data.types.EventTypes

data class BaseEvent(
    val eventType : EventTypes,
    val payload: Map<String, Any>? = null
)
