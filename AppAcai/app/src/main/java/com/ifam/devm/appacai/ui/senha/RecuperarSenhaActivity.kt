package com.ifam.devm.appacai.ui.senha

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UserViewModel
import com.ifam.devm.appacai.repository.room.AppDatabase
import kotlinx.android.synthetic.main.activity_recuperar_senha.*
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RecuperarSenhaActivity : AppCompatActivity() {
    //variaveis a serem utilizadas
    private lateinit var email: String
    private lateinit var recuperarSenhaViewModel: UserViewModel //variavel para uso da viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)

        toolbarDadosLojaAlterarSenha.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        btContinuar.setOnClickListener {
            recUserLayoutInpTextEmail.error = null

            email = txtEmailecuperar.text.toString()//variavel email recebe conteudo do campo email

            it.hideKeyboard()

            //valida email
            if (validarEmail()) {
                irProximoPasso()
            }
        }

    }

    //Esconde teclado
    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun validarEmail(): Boolean {
        if (email.isEmpty()) {
            recUserLayoutInpTextEmail.error = "Insira um e-mail!"
            return false
            // se o Email não tiver formato válido, uma mensagem de erro é emitida, senão segue com o cadastro
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            recUserLayoutInpTextEmail.error = "E-mail inválido!"
            return false
        }
        return true
    }

    private fun irProximoPasso() {
        //coleta dados do banco
        doAsync {
            recuperarSenhaViewModel =
                UserViewModel(AppDatabase.getDatabase(this@RecuperarSenhaActivity))
            val usuario = recuperarSenhaViewModel.consultarLoginExistente(email)
            try {
                Log.d("email", usuario.email)

                irProximoPasso(usuario)

            } catch (ex: Exception) {
                Log.d("FAZ.LOG", ex.toString())
                uiThread {
                    recUserLayoutInpTextEmail.error = "Email não cadastrado!"
                }
            }
        }
    }

    //Verifica se tem CPF igual ao do banco
    private fun AnkoAsyncContext<RecuperarSenhaActivity>.irProximoPasso(usuario: Usuario) {
        if (usuario.email != email) {
            uiThread {
                recUserLayoutInpTextEmail.error = "Email não cadastrado!"
            }
        } else {
            uiThread {
//              escreverNoSharedPreferences(usuario)
                showMessageToast("Email encontrado!")
                startActivity(Intent(this@RecuperarSenhaActivity, AlterarSenhaActivity::class.java))
            }
        }
    }

    //Toas pronto
    private fun showMessageToast(texto : String) {
        Toast.makeText(this@RecuperarSenhaActivity, texto, Toast.LENGTH_SHORT)
            .show()
    }

}