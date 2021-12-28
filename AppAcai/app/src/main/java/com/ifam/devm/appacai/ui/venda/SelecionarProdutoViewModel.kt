package com.ifam.devm.appacai.ui.venda

import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.ProdutoVenda
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.ProdutoVendaRepository

class SelecionarProdutoViewModel(appDatabase: AppDatabase) {
    private var produtoVendaRepository = ProdutoVendaRepository(appDatabase)
    private var produtoVendaList: List<ProdutoVenda>

    init {
        produtoVendaList = produtoVendaRepository.getAllProdutosVenda()
    }

    fun deleteAllProdVenda() {
        return produtoVendaRepository.deleteAllProdVenda()
    }

    fun getAllProdutoVenda(): List<ProdutoVenda> {
        return produtoVendaRepository.getAllProdutosVenda()
    }
}