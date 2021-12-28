package com.ifam.devm.appacai.ui.funcionarios

import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.FuncionarioRepository

class CadastrarFuncionarioViewModel(appDataBase: AppDatabase) {
    private var funcionarioRepository = FuncionarioRepository(appDataBase)
    private lateinit var mostrarDados: Funcionario
    private var funcionarioList: List<Funcionario>

    init {
        funcionarioList = funcionarioRepository.getAllFuncionario()
    }

    fun loadFuncionarioData(n : String){
        mostrarDados = funcionarioRepository.funcionarioByNome(n)
    }

    fun getFuncionarioData(): Funcionario {
        return mostrarDados
    }

    //consulta funcionario existente
    fun consutaFuncionarioPorCPF(cpf: String): Funcionario {
        return funcionarioRepository.funcionarioByCpf(cpf)
    }

    fun consultaFuncionarioPorNome(nome: String): Funcionario {
        return funcionarioRepository.funcionarioByNome(nome)
    }

    fun getAllFuncionarios(): List<Funcionario>{
        return funcionarioRepository.getAllFuncionario()
    }

    fun deleteAllFuncionarios() {
        return funcionarioRepository.deleteAllFuncionarios()
    }
}