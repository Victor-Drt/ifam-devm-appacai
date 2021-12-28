package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.ProdutoVenda

interface ProdutoVendaDataSource : BaseDataSource<ProdutoVenda> {
    fun getAllProdutosVenda() : List<ProdutoVenda>
    fun produtoVendaById(id : Long) : ProdutoVenda
    fun getProdutoVenda() : ProdutoVenda
    fun getProdutoVendaValor() : List<Float>
    fun produtoVendaByNome(nome:String) : ProdutoVenda
}