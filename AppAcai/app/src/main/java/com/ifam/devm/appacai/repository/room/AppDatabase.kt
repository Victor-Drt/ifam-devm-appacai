package com.ifam.devm.appacai.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ifam.devm.appacai.model.*
import com.ifam.devm.appacai.repository.room.dao.*
import com.ifam.devm.appacai.repository.sqlite.DATABASE_NAME
import com.ifam.devm.appacai.repository.sqlite.DATABASE_VERSION
import org.jetbrains.anko.doAsync

@Database(
    entities = [
        Usuario::class,
        Produto::class,
        ProdutoVenda::class,
        RelatorioVenda::class,
        Pagamento::class,
        Funcionario::class
    ], version = DATABASE_VERSION, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun produtoDao(): ProdutoDao
    abstract fun produtoVendaDao(): ProdutoVendaDao
    abstract fun relatorioVendaDao(): RelatorioVendaDao
    abstract fun funcionarioDao(): FuncionarioDao
    abstract fun pagamentoDao(): PagamentoDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        )
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                    doAsync {
//                                        PREPOPULATE_FUNCIONARIO.forEach {
//                                            getDatabase(context).funcionarioDao().insert(it)
//                                        }
                                    }
                                }
                            }).build()
                }
            }
            return instance as AppDatabase
        }

//        val PREPOPULATE_FUNCIONARIO = listOf(
//            Funcionario(
//                1, "João", "001.002.003-04", "+55 (92) 987654321"
//            )
//        )
    }
}