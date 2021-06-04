package com.example.miketoide.domain.persistence.abstractions

interface IAddCollection<TEntity> {
    fun upsert(document : TEntity)
    fun upsertMany(documents: Collection<TEntity>)
}