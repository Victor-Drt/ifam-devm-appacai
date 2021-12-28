package com.ifam.devm.appacai.repository.room

import com.ifam.devm.appacai.model.ProdutoVenda
import com.ifam.devm.appacai.repository.ProdutoVendaDataSource

class ProdutoVendaRepository (database: AppDatabase) : ProdutoVendaDataSource {
    private val produtoVendaDao = database.produtoVendaDao()

    fun deleteAllProdVenda() {
        return produtoVendaDao.deleteAllProdVenda()
    }

    override fun getProdutoVenda(): ProdutoVenda {
        return produtoVendaDao.getProdutoVenda()
    }

    override fun getProdutoVendaValor(): List<Float> {
        return produtoVendaDao.getProdutoVendaValor()
    }

    override fun getAllProdutosVenda(): List<ProdutoVenda> {
        return produtoVendaDao.getAllProdutosVenda()
    }

    override fun produtoVendaById(id: Long): ProdutoVenda {
        return produtoVendaDao.produtoVendaById(id)
    }

    override fun produtoVendaByNome(nome: String): ProdutoVenda {
        return produtoVendaDao.produtoVendaByNome(nome)
    }

    override fun save(obj: ProdutoVenda) {
        if (obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: ProdutoVenda): Long {
        return produtoVendaDao.insert(obj)
    }

    override fun update(obj: ProdutoVenda) {
        return produtoVendaDao.update(obj)
    }

    override fun delete(obj: ProdutoVenda) {
        return produtoVendaDao.delete(obj)
    }
}