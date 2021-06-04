package com.example.miketoide.domain.persistence.abstractions

import io.objectbox.Property

interface IReadWithOrder<TEntity> {
    fun getOrdered(property : Property<TEntity>) : Collection<TEntity>
}