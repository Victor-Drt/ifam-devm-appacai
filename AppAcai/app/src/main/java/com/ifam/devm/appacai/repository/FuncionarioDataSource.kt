package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Usuario

interface FuncionarioDataSource:BaseDataSource<Funcionario> {
    fun funcionarioEstaRegistrado(email: String): Funcionario
    fun getFuncionario(): Funcionario
    fun funcionarioId(): Long
    fun funcionarioByCpf(cpf: String): Funcionario?
    fun funcionarioByNome(nome: String): Funcionario?
}