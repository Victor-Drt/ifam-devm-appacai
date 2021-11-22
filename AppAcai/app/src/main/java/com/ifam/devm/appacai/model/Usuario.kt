package com.ifam.devm.appacai.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifam.devm.appacai.repository.sqlite.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_USUARIO)
data class Usuario (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = COLUMN_ID)
        var id_usuario: Long = 0,
        @ColumnInfo(name = COLUMN_NOME)
        var nomeUsuario: String = "",
        @ColumnInfo(name = COLUMN_NOME_EMPRESA)
        var nomeEmpresa: String = "",
        @ColumnInfo(name = COLUMN_EMAIL)
        var email: String = "",
        @ColumnInfo(name = COLUMN_SENHA)
        var senha: String = "",
        @ColumnInfo(name = COLUMN_CHAVEPIX)
        var chavePix: String = "",
) : Parcelable