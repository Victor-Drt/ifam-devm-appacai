package com.ifam.devm.appacai.repository

interface BaseDataSource<T> {
    fun save(obj: T)
    fun insert(obj: T): Long
    fun update(obj: T)
    fun delete(obj: T)
}