package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.sqlite.*

@Dao
interface ProdutoDao : BaseDao<Produto> {
    //    retorna lista de produtos
    @Query("""SELECT * FROM $TABLE_PRODUTO""")
    fun getAllProduto(): List<Produto>

    //    retorna um produto
    @Query("""SELECT * FROM $TABLE_PRODUTO""")
    fun getProduto(): Produto

    // retorna produto pelo id
    @Query("""SELECT * FROM $TABLE_PRODUTO WHERE $COLUMN_ID = :id""")
    fun produtoById(id: Long): Produto

    //    retorna produto pelo nome
    @Query("""SELECT * FROM $TABLE_PRODUTO WHERE $COLUMN_NOME = :nome""")
    fun produtoByNome(nome: String): Produto

    //    retorna produto pela frequencia de vendas
    @Query("""SELECT * FROM $TABLE_PRODUTO WHERE $COLUMN_FREQ_VENDAS = :freqVendas""")
    fun produtoByFreqVendas(freqVendas: Int): Produto

    //    retorna produto pela avaliacao
    @Query("""SELECT * FROM $TABLE_PRODUTO WHERE $COLUMN_AVALIACAO = :aval""")
    fun produtoByAvaliacao(aval: Float): Produto

    //    retorna produto por valor
    @Query("""SELECT * FROM $TABLE_PRODUTO WHERE $COLUMN_AVALIACAO = :valor""")
    fun produtoByValor(valor: Float): Produto

}