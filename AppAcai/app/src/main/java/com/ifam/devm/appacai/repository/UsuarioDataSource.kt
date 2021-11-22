package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Usuario

interface UsuarioDataSource: BaseDataSource<Usuario>  {
    fun usuarioEstaRegistrado(email: String): Usuario
    fun getUsuario(): Usuario
    fun usuarioId(): Long
    fun usuarioByEmailESenha(email: String, senha: String): Usuario?
    fun usuarioByNome(cpf: String): Usuario?
}