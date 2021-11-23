package com.ifam.devm.appacai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.ui.cadastro_user.CadastrarUsuarioActivity
import com.ifam.devm.appacai.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btTelaCadastro.setOnClickListener {
            startActivity(Intent(this, CadastrarUsuarioActivity::class.java))
        }

        btTelaLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}