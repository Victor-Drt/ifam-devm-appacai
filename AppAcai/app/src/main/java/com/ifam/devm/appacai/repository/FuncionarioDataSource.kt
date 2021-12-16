package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Funcionario

interface FuncionarioDataSource:BaseDataSource<Funcionario> {
    fun getFuncionario(): Funcionario
    fun funcionarioId(): Long
    fun funcionarioByCpf(cpf: String): Funcionario?
}