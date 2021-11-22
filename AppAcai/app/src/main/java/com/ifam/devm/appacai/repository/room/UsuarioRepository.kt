package com.ifam.devm.appacai.repository.room

import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UsuarioDataSource

class UsuarioRepository(database: AppDatabase): UsuarioDataSource {
    private val usuarioDao = database.usuarioDao()

    //Função que recebe o valor do id do usuario
    override fun usuarioId(): Long = usuarioDao.usuarioIds()

    override fun usuarioByEmailESenha(email: String, senha: String): Usuario? {
        return usuarioDao.usuarioByEmailESenha(email, senha)
    }

    override fun usuarioByNome(nome: String): Usuario? {
        return usuarioDao.getUsuario(nome)
    }

    fun getDadosUsuario(): Usuario {
        return usuarioDao.getDadosUsuario()
    }

    override fun getUsuario(): Usuario {
        return usuarioDao.getUsuario()
    }

    //VERIFICA O REGISTRO DO USUARIO USANDO O EMAIL COMO PARAMETRO
    override fun usuarioEstaRegistrado(email: String): Usuario {
        return usuarioDao.getUsuario(email)
    }

    fun getAllUsuario(): List<Usuario> {
        return usuarioDao.getAllUsuario()
    }

    /**
    FUNÇOES DE MANIPULAÇAO DO BANCO
     **/
    override fun save(obj: Usuario) {
        if(obj.id_usuario == 0L){
            val id = insert(obj)
            obj.id_usuario = id
        } else{
            update(obj)
        }
    }

    override fun insert(obj: Usuario): Long {
        return usuarioDao.insert(obj)
    }

    override fun update(obj: Usuario) {
        return usuarioDao.update(obj)
    }

    override fun delete(obj: Usuario) {
        return usuarioDao.delete(obj)
    }
}