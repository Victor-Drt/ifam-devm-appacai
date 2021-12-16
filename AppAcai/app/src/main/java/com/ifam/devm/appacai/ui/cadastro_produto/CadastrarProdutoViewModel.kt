package com.ifam.devm.appacai.ui.cadastro_produto

import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.room.ProdutoRepository

class CadastrarProdutoViewModel(appDatabase: AppDatabase) {
    private var produtoRepository = ProdutoRepository(appDatabase)
    private var produtoList: List<Produto>

    init {
        produtoList = produtoRepository.getAllProduto()
    }

    //       acessa repositório de produto para deletar um novo
    fun consultarProdutoExistente(nome : String): Produto {
        return produtoRepository.produtoByNome(nome)
    }

    fun getAllProduto(): List<Produto> {
        return produtoRepository.getAllProduto()
    }

}