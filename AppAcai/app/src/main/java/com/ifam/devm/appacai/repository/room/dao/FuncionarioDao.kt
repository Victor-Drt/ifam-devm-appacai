package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.sqlite.*

@Dao
interface FuncionarioDao: BaseDao<Funcionario> {

    @Query("SELECT * FROM $TABLE_FUNCIONARIO")
    fun getAllFuncionario(): List<Funcionario>

    @Query("SELECT * FROM $TABLE_FUNCIONARIO")
    fun getFuncionario(): Funcionario

    @Query("""  SELECT * FROM $TABLE_FUNCIONARIO WHERE $COLUMN_CPF_FUNCIONARIO =  :cpf """ )
    fun getFuncionarioByEmail(cpf: String): Funcionario

    @Query( """  SELECT * FROM $TABLE_FUNCIONARIO WHERE $COLLUMN_ID_FUNCIONARIO = :id """)
    fun getFuncionarioByID(id: Long): Funcionario

    @Query("SELECT id_funcionario FROM $TABLE_FUNCIONARIO LIMIT 1")
    fun funcionarioIds(): Long

    @Query(""" SELECT * FROM $TABLE_FUNCIONARIO """)
    fun getFuncio(): Funcionario
}