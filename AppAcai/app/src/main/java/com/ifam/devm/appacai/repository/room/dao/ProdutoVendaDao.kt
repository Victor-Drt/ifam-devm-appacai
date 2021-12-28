package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.ProdutoVenda
import com.ifam.devm.appacai.repository.sqlite.COLUMN_ID
import com.ifam.devm.appacai.repository.sqlite.COLUMN_NOME
import com.ifam.devm.appacai.repository.sqlite.COLUMN_VALOR
import com.ifam.devm.appacai.repository.sqlite.TABLE_PRODUTO_VENDA

@Dao
interface ProdutoVendaDao : BaseDao<ProdutoVenda>{
    @Query("""SELECT * FROM $TABLE_PRODUTO_VENDA""")
    fun getAllProdutosVenda(): List<ProdutoVenda>

    //    retorna um produto
    @Query("""SELECT * FROM $TABLE_PRODUTO_VENDA""")
    fun getProdutoVenda(): ProdutoVenda

    // retorna produto pelo id
    @Query("""SELECT * FROM $TABLE_PRODUTO_VENDA WHERE $COLUMN_ID = :id""")
    fun produtoVendaById(id: Long): ProdutoVenda

    //    retorna produto pelo nome
    @Query("""SELECT * FROM $TABLE_PRODUTO_VENDA WHERE $COLUMN_NOME = :nome""")
    fun produtoVendaByNome(nome: String): ProdutoVenda

//    retorna a lista de valores dos produtos
    @Query("""SELECT $COLUMN_VALOR FROM $TABLE_PRODUTO_VENDA""")
    fun getProdutoVendaValor(): List<Float>

    //    retorna produto por valor
    @Query("""DELETE FROM $TABLE_PRODUTO_VENDA""")
    fun deleteAllProdVenda()
}