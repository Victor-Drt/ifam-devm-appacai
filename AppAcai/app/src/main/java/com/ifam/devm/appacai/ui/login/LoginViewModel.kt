package com.ifam.devm.appacai.ui.login

import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.UsuarioRepository

class LoginViewModel (appDatabase: AppDatabase){
    private var usuarioRepository = UsuarioRepository(appDatabase)

//    consulta login atraves do email
    fun consultarLoginExistente(email:String): Usuario {
        return usuarioRepository.usuarioEstaRegistrado(email)
    }
}