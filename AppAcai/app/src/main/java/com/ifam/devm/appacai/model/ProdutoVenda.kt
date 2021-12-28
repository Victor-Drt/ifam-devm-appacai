package com.ifam.devm.appacai.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifam.devm.appacai.repository.sqlite.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_PRODUTO_VENDA)
data class ProdutoVenda(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_ID_VENDA, index = true) var id_venda: Long = 0,
    @ColumnInfo(name = COLUMN_NOME) var nome: String = "",
    @ColumnInfo(name = COLUMN_DESCRICAO) var descricao: String = "",
    @ColumnInfo(name = COLUMN_VALOR) var valor: Float = 0.00f,
    @ColumnInfo(name = COLUMN_FOTO) var foto: ByteArray? = null,
) : Parcelable