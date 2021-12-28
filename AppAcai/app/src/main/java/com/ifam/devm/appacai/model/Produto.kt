package com.ifam.devm.appacai.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifam.devm.appacai.repository.sqlite.*
import kotlinx.android.parcel.Parcelize
import java.sql.Blob

@Parcelize
@Entity(tableName = TABLE_PRODUTO)
data class Produto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_NOME) var nome: String = "",
    @ColumnInfo(name = COLUMN_DESCRICAO) var descricao: String = "",
    @ColumnInfo(name = COLUMN_TIPO) var tipo: String = "",
    @ColumnInfo(name = COLUMN_VALOR) var valor: Float = 0.00f,
    @ColumnInfo(name = COLUMN_AVALIACAO) var avaliacao: Float = 0.0f,
    @ColumnInfo(name = COLUMN_QTD_VOTOS) var qtdVotos: Int = 0,
    @ColumnInfo(name = COLUMN_FOTO) var foto: ByteArray? = null,
    @ColumnInfo(name = COLUMN_FREQ_VENDAS) var freqVenda: Int = 0
) : Parcelable