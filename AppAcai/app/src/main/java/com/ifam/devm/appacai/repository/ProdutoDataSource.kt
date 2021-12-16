package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Produto

interface ProdutoDataSource : BaseDataSource<Produto>{
    fun getAllProduto() : List<Produto>
    fun produtoById(id : Long) : Produto
    fun produtoByNome(nome: String) : Produto?
    fun produtoByFreqVendas(frequenciaVendas : Int) : Produto?
    fun produtoByAvaliacao(aval : Float) : Produto
    fun produtoByValor(valor : Float) : Produto
}