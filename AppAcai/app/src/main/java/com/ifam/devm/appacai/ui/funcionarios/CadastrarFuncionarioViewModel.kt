package com.ifam.devm.appacai.ui.funcionarios

import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.FuncionarioRepository

class CadastrarFuncionarioViewModel(appDataBase: AppDatabase) {
    private var funcionarioRepository = FuncionarioRepository(appDataBase)
    private var funcionarioList: List<Funcionario>

    init {
        funcionarioList = funcionarioRepository.getAllFuncionario()
    }

    //consulta funcionario existente
    fun consutaFuncionarioPorCPF(cpf: String): Funcionario? {
        return funcionarioRepository.funcionarioByCpf(cpf)

    }

    fun getAllFuncionarios(): List<Funcionario>{
        return funcionarioRepository.getAllFuncionario()
    }
}