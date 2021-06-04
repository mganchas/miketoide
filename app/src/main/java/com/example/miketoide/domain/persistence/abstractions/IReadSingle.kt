package com.example.miketoide.domain.persistence.abstractions

interface IReadSingle<TEntity> {
    fun getSingle() : TEntity
    fun getSingleOrNull() : TEntity?
}