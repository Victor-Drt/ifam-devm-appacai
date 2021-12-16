package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.Funcionario

import com.ifam.devm.appacai.repository.sqlite.COLLUMN_ID_FUNCIONARIO
import com.ifam.devm.appacai.repository.sqlite.COLUMN_CPF_FUNCIONARIO
import com.ifam.devm.appacai.repository.sqlite.TABLE_FUNCIONARIO

@Dao
interface FuncionarioDao: BaseDao<Funcionario> {

    @Query("SELECT * FROM $TABLE_FUNCIONARIO")
    fun getAllfuncionarios(): List<Funcionario>

    @Query("SELECT * FROM $TABLE_FUNCIONARIO")
    fun  getFuncionario(): Funcionario

    @Query(""" SELECT * FROM $TABLE_FUNCIONARIO WHERE $COLUMN_CPF_FUNCIONARIO= :cpf """)
    fun getFuncionarioEmail(cpf: String): Funcionario

    @Query(""" SELECT * FROM $TABLE_FUNCIONARIO WHERE $COLLUMN_ID_FUNCIONARIO= :id """)
    fun getFucionarioId(id: Long):Funcionario

    @Query("SELECT id_funcionario FROM $TABLE_FUNCIONARIO LIMIT 1")
    fun funcionariosIds(): Long

    @Query(""" SELECT * FROM $TABLE_FUNCIONARIO""")
    fun getFuncionarios():Funcionario

}