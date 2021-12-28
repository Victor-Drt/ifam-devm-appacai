package com.ifam.devm.appacai.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifam.devm.appacai.repository.sqlite.COLUMN_DATA_PAGAMENTO
import com.ifam.devm.appacai.repository.sqlite.COLUMN_ID
import com.ifam.devm.appacai.repository.sqlite.COLUMN_VALOR
import com.ifam.devm.appacai.repository.sqlite.TABLE_PAGAMENTO
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Entity(tableName = TABLE_PAGAMENTO)
class Pagamento (@PrimaryKey(autoGenerate = true)
                 @ColumnInfo(name = COLUMN_ID) var id: Long = 0,
                 @ColumnInfo(name = COLUMN_VALOR) var valor: Float = 0f,
                 @ColumnInfo(name = COLUMN_DATA_PAGAMENTO) var dataPagamento: String = "",
){
    object dataHoje{
        val formataData = SimpleDateFormat("dd-MM-yyyy")
        var data = Date()
        var dataFormatada = formataData.format(data)  // variável que armazena o total recebido no dia
    }

    object caixaInicial{
        var valorCaixaInicial: Float =0f
    }
}