package com.ifam.devm.appacai.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.dao.UsuarioDao
import com.ifam.devm.appacai.repository.sqlite.DATABASE_NAME
import com.ifam.devm.appacai.repository.sqlite.DATABASE_VERSION
import org.jetbrains.anko.doAsync

@Database(entities = [
    Usuario::class
], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if(instance == null) {
                synchronized(this) {
                    instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        )
                            .addCallback(object: Callback(){
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                }
                            }).build()
                }
            }
            return instance as AppDatabase
        }
    }
}