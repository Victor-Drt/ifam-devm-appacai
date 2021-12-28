package com.ifam.devm.appacai.repository

import com.ifam.devm.appacai.model.Pagamento
import com.ifam.devm.appacai.repository.room.AppDatabase

class PagamentoRepository(database: AppDatabase): PagamentoDataSource {
    private val pagamentoDao = database.pagamentoDao()
    override fun getAllPagamentos(): List<Pagamento> {
        return pagamentoDao.getAllPagamentos()
    }

    override fun getPagamentoByDia(data: String): List<Pagamento> {
        return pagamentoDao.getPagamentoByDia(data)
    }

    override fun save(obj: Pagamento) {
        // se id == 0 significa que foi instanciado com o valor padrão
        if (obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: Pagamento): Long {
        return pagamentoDao.insert(obj)
    }

    override fun update(obj: Pagamento) {
        return pagamentoDao.update(obj)
    }

    override fun delete(obj: Pagamento) {
        return pagamentoDao.delete(obj)
    }
}