package com.ifam.devm.appacai.repository.room

import com.ifam.devm.appacai.model.RelatorioVenda
import com.ifam.devm.appacai.repository.RelatorioVendaDataSource

class RelatorioVendaRepository(database: AppDatabase): RelatorioVendaDataSource {
    private var relatorioVendaDao = database.relatorioVendaDao()

    override fun getRelatorioVenda(): RelatorioVenda {
        return relatorioVendaDao.getRelatorioVenda()
    }

    override fun save(obj: RelatorioVenda) {
        if(obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: RelatorioVenda): Long {
        return relatorioVendaDao.insert(obj)
    }

    override fun update(obj: RelatorioVenda) {
        return relatorioVendaDao.update(obj)
    }

    override fun delete(obj: RelatorioVenda) {
        return relatorioVendaDao.delete(obj)
    }
}