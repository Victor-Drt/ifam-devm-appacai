package com.ifam.devm.appacai.repository.room

import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.ProdutoDataSource

class ProdutoRepository(database: AppDatabase) : ProdutoDataSource {
    private val produtoDao = database.produtoDao()

    /**
     * funcoes do data source
     *
     * **/
    fun deleteAllProdutos() {
        return produtoDao.deleteAllProd()
    }


    override fun getAllProduto(): List<Produto> {
        return produtoDao.getAllProduto()
    }

    override fun produtoById(id: Long): Produto {
        return produtoDao.produtoById(id)
    }

    override fun produtoByNome(nome: String): Produto {
        return produtoDao.produtoByNome(nome)
    }

    override fun produtoByFreqVendas(frequenciaVendas: Int): Produto? {
        return produtoDao.produtoByFreqVendas(frequenciaVendas)
    }

    override fun produtoByAvaliacao(aval: Float): Produto {
        return produtoDao.produtoByAvaliacao(aval)
    }

    override fun produtoByValor(valor: Float): Produto {
        return produtoDao.produtoByValor(valor)
    }

    /**
     * funcoes basicas do banco
     *
     * **/

    //    salva produto no banco
    override fun save(obj: Produto) {
        if (obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    //    insere produto
    override fun insert(obj: Produto): Long {
        return produtoDao.insert(obj)
    }

    //    atualiza produto
    override fun update(obj: Produto) {
        return produtoDao.update(obj)
    }

    //    deleta produto
    override fun delete(obj: Produto) {
        return produtoDao.delete(obj)
    }

}