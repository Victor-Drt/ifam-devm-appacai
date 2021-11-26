package com.ifam.devm.appacai.ui.senha

import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.UsuarioRepository

class RecuperarSenhaViewModel (appDatabase: AppDatabase) {
    private var usuarioRepository = UsuarioRepository(appDatabase)
    private lateinit var mostrarDados: Usuario //coletor dos dados

    // Funçõs do view model
    fun consultarLoginExistente(email: String): Usuario {
        return usuarioRepository.usuarioEstaRegistrado(email)
    }

    fun carregaDadosALTERAR() {
        mostrarDados = usuarioRepository.getUsuario()
    }

    fun pegaDadosUsuario(): Usuario {
        return mostrarDados
    }

    fun salvarLoginUsuario(objUsuario: Usuario) {
        return usuarioRepository.save(objUsuario)
    }

    fun recuperarTodosUsuarios(): List<Usuario> {
        return usuarioRepository.getAllUsuario()
    }

    fun atualizaSenha(objUsuario: Usuario) {
        return usuarioRepository.update(objUsuario)
    }
}