package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.FuncionarioRepository

class FuncionarioViewModel (appDataBase: AppDatabase) {
    private var funcionarioRepository = FuncionarioRepository(appDataBase)
    private  lateinit var mostrarDados: Funcionario

    fun pesquisaCPF(cpf: String): Funcionario? {
        return funcionarioRepository.funcionarioByCpf(cpf)
    }

    fun novoFuncionario(funcionario: Funcionario){
        return funcionarioRepository.save(funcionario)
    }

    fun listaTodosFuncionarios(): List<Funcionario>{
        return funcionarioRepository.getAllFuncionario()
    }


}