package com.ifam.devm.appacai.ui.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_user.CadastrarUsuarioActivity
import com.ifam.devm.appacai.ui.home.HomeActivity
import com.ifam.devm.appacai.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_startup.*
import org.jetbrains.anko.doAsync

class StartupActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        btTelaCadastro.setOnClickListener {
            startActivity(Intent(this, CadastrarUsuarioActivity::class.java))
        }

        btTelaLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}