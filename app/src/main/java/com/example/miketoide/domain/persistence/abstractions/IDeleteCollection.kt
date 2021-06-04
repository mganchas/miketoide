package com.example.miketoide.domain.persistence.abstractions

interface IDeleteCollection<TEntity> {
    fun delete(document : TEntity)
    fun deleteById(documentId : Long)
    fun deleteMany(documents: Collection<TEntity>)
    fun deleteAll()
}