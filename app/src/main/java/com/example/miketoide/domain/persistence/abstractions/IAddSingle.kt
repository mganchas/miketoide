package com.example.miketoide.domain.persistence.abstractions

interface IAddSingle<TEntity> {
    fun upsert(document : TEntity)
}