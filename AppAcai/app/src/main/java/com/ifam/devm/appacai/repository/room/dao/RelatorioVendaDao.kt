package com.ifam.devm.appacai.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifam.devm.appacai.model.RelatorioVenda
import com.ifam.devm.appacai.repository.sqlite.TABLE_VENDA

@Dao
interface RelatorioVendaDao :  BaseDao<RelatorioVenda> {

    @Query("SELECT * FROM $TABLE_VENDA")
    fun getRelatorioVenda(): RelatorioVenda

}