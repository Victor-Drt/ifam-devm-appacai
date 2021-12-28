package com.ifam.devm.appacai.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifam.devm.appacai.repository.sqlite.*
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Entity(tableName = TABLE_VENDA)
data class RelatorioVenda(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_NOME_EMPRESA) var nomeLoja: String = "",
    @ColumnInfo(name = COLUMN_DATA_RELATORIO) var dataRelatorio: String = "",
    @ColumnInfo(name = COLUMN_VALOR_DIA) var valorDia: Double = 0.00,
    @ColumnInfo(name = COLUMN_VALOR_SEMANA) var valorSemana: Double = 0.00,
    @ColumnInfo(name = COLUMN_AVAL_PRODUTO) var mediaAvalProd: Float = 0.00f,
    @ColumnInfo(name = COLUMN_FUNC_SEMANA) var funcSemana: String = "",
    @ColumnInfo(name = COLUMN_PROD_MAIS_VENDIDOS) var maisVendidos: String = "",
) {
    object dataHoje{
        @RequiresApi(Build.VERSION_CODES.O)
        var dataHoje: LocalDate = LocalDate.now()   // variável que armazena o total recebido no dia
    }

    object caixaInicial{
        var valorCaixaInicial: Float =0.00f
    }
}