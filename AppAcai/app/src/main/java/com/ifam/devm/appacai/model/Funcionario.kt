package com.ifam.devm.appacai.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifam.devm.appacai.repository.sqlite.*
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = TABLE_FUNCIONARIO)
data class Funcionario (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name =  COLLUMN_ID_FUNCIONARIO)
    var id_funcionario: Long = 0,

    @ColumnInfo(name = COLUMN_NOME_FUNCIONARIO)
    var nome_funcionario: String = "",

    @ColumnInfo(name = COLUMN_EMAIL_FUNCIONARIO)
    var email_funcionario: String = "",

    @ColumnInfo(name = COLUMN_TELEFONE_FUNCIONARIO)
    var telefone_funcionario: String = "",

    @ColumnInfo(name= COLUMN_CPF_FUNCIONARIO)
    var cpf_funcionario: String = "",

    @ColumnInfo(name= COLUMN_META_VENDA)
    var meta_vendas: Double,

    @ColumnInfo(name= COLUMN_TOTAL_VENDA)
    var total_vendas: Double,
    @ColumnInfo(name = COLUMN_SENHA_FUNCIONARIO)
    var senha: String
): Parcelable

