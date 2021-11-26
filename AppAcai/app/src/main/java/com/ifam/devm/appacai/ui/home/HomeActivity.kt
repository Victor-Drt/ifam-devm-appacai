package com.ifam.devm.appacai.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.senha.RecuperarSenhaViewModel
import com.ifam.devm.appacai.ui.startup.StartupActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class HomeActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario
    private lateinit var viewModel: RecuperarSenhaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btLogOff.setOnClickListener {
            val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
            val sharedEditor = sharedPreferences.edit()
            sharedEditor.putString("login", "")
            sharedEditor.apply()
            startActivity(Intent(this@HomeActivity, StartupActivity::class.java))
            finishAffinity()
        }
    }

    override fun onStart() {
        carregaNomeUsuarioDoBanco()
        super.onStart()
    }

    private fun carregaNomeUsuarioDoBanco() {
        doAsync {
            viewModel =
                RecuperarSenhaViewModel(AppDatabase.getDatabase(this@HomeActivity))
            viewModel.carregaDadosALTERAR()
            uiThread {
                usuario = viewModel.pegaDadosUsuario()
                try {
                    val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
                    txtUsername.text =
                        sharedPreferences.getString("nome", "")
                } catch (e: Exception) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Nao foi possivel carregar o nome",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}