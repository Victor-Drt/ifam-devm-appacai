package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.Pagamento
import com.ifam.devm.appacai.repository.sqlite.COLUMN_DATA_PAGAMENTO
import com.ifam.devm.appacai.repository.sqlite.TABLE_PAGAMENTO

@Dao
interface PagamentoDao: BaseDao<Pagamento> {
    /**
     * Obtém todos os pagamentos cadastrados
     */
    @Query("""SELECT * FROM $TABLE_PAGAMENTO""")
    fun getAllPagamentos(): List<Pagamento>

    @Query("""SELECT * FROM $TABLE_PAGAMENTO WHERE $COLUMN_DATA_PAGAMENTO = :data""")
    fun getPagamentoByDia(data:String) : List<Pagamento>

    @Query("""DELETE FROM $TABLE_PAGAMENTO""")
    fun deleteAll()

}