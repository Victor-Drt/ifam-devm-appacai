package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.sqlite.*

@Dao
interface UsuarioDao: BaseDao<Usuario> {

//    retorna lista de usuarios
    @Query("SELECT * FROM $TABLE_USUARIO")
    fun getAllUsuario(): List<Usuario>

    @Query("DELETE FROM $TABLE_USUARIO")
    fun deleteAll()

//    retorna o usuario
    @Query("SELECT * FROM $TABLE_USUARIO")
    fun getDadosUsuario(): Usuario

//    PEGA USUARIO USANDO O EMAIL
    @Query("""SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = :email""")
    fun getUsuario(email: String): Usuario

//        PEGA USUARIO USANDO O ID
    @Query("""SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_ID = :id_usuario""")
    fun usuarioById(id_usuario: Long): Usuario

    /**
     * Recupera um Estabelecendo através do email e senha
     */
    @Query("""SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = :email AND $COLUMN_SENHA = :senha""")
    fun usuarioByEmailESenha(email: String, senha: String): Usuario?

    //Faz a consulta do id na tabela
    @Query("SELECT id FROM $TABLE_USUARIO LIMIT 1")
    fun usuarioIds(): Long

    @Query("""SELECT * FROM $TABLE_USUARIO""")
    fun getUsuario(): Usuario
}