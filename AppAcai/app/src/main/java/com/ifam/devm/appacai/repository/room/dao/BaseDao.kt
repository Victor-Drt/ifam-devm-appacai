package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    /**
     * Insere um objeto no banco de dados
     *
     * @param obj o objeto a ser inserido
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    /**
     * Atualiza um objeto do banco de dados
     *
     * @param obj objeto a ser atualizado
     */
    @Update
    fun update(obj: T)

    /**
     * Remove um objeto do banco de dados
     *
     * @param obj o objeto a ser removido
     */
    @Delete
    fun delete(obj: T)
}