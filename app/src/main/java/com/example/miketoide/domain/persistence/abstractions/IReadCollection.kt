package com.example.miketoide.domain.persistence.abstractions

interface IReadCollection<TEntity> {
    fun isEmpty() : Boolean
    fun getAll() : Collection<TEntity>
}