package com.example.miketoide.domain.events

import com.example.miketoide.data.events.BaseEvent

interface IEventApi {
    fun publish(event: BaseEvent)
    fun register(component: Any)
    fun unregister(component: Any)
}