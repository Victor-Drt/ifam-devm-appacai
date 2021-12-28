package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.RelatorioVenda

interface RelatorioVendaDataSource :  BaseDataSource<RelatorioVenda> {
    fun getRelatorioVenda(): RelatorioVenda
}