package com.ifam.devm.appacai.repository.room

import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.FuncionarioDataSource

class FuncionarioRepository(database: AppDatabase): FuncionarioDataSource {
    private val funcionarioDao = database.funcionarioDao()

    override fun funcionarioId(): Long = funcionarioDao.funcionarioIds()

    override fun funcionarioByCpf(cpf: String): Funcionario? {
        return funcionarioDao.getFuncionarioByEmail(cpf)
    }

    override fun getFuncionario(): Funcionario {
       return funcionarioDao.getFuncionario()
    }

    fun getAllFuncionario(): List<Funcionario>{
        return funcionarioDao.getAllFuncionario()
    }

    override fun save(obj: Funcionario) {
        if(obj.id_funcionario == 0L){
            val id = insert(obj)
            obj.id_funcionario = id
        }else{
            update(obj)
        }
    }

    override fun insert(obj: Funcionario): Long {
        return funcionarioDao.insert(obj)
    }

    override fun update(obj: Funcionario) {
        return funcionarioDao.update(obj)
    }

    override fun delete(obj: Funcionario) {
        return funcionarioDao.delete(obj)
    }


}