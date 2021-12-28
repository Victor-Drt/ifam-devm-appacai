package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Pagamento

interface PagamentoDataSource : BaseDataSource<Pagamento> {
    fun getAllPagamentos(): List<Pagamento>
    fun getPagamentoByDia(data: String): List<Pagamento>
}