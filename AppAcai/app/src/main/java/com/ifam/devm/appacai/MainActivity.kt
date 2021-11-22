package com.ifam.devm.appacai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.ui.cadastro_user.CadastrarUsuarioActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        telaDeCadastro.setOnClickListener {
            startActivity(Intent(this, CadastrarUsuarioActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
// teste
    }

}