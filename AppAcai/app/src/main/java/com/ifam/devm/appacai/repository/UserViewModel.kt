package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.UsuarioRepository

class UserViewModel (appDatabase: AppDatabase) {
    private var usuarioRepository = UsuarioRepository(appDatabase)
    private lateinit var mostrarDados: Usuario //coletor dos dados

    //    consulta login atraves do email
    fun consultarLoginExistente(email:String): Usuario {
        return usuarioRepository.usuarioEstaRegistrado(email)
    }

    fun carregaDadosALTERAR() {
        mostrarDados = usuarioRepository.getUsuario()
    }

    fun CarregaDadosUsuario(){
        mostrarDados = usuarioRepository.getDadosUsuario()
    }

    fun getTodosDadosUsuario(): Usuario{
        return mostrarDados
    }

    fun salvarLoginUsuario(objUsuario: Usuario) {
        return usuarioRepository.save(objUsuario)
    }

    fun recuperarTodosUsuarios(): List<Usuario> {
        return usuarioRepository.getAllUsuario()
    }

    // Editar Usuario
    fun CarregaDadosEDITAR() {
        mostrarDados = usuarioRepository.getUsuario()
    }

    fun pegaDadosUsuario(): Usuario {
        return mostrarDados
    }

    fun atualizarDados(usuario: Usuario) {
        usuarioRepository.update(usuario)
    }

    fun removerDados(usuario: Usuario) {
        usuarioRepository.delete(usuario)
    }

    // Senha
    fun atualizaSenha(objUsuario: Usuario) {
        return usuarioRepository.update(objUsuario)
    }

}