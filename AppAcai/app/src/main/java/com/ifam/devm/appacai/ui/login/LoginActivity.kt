package com.ifam.devm.appacai.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UserViewModel
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.home.HomeActivity
import com.ifam.devm.appacai.ui.senha.RecuperarSenhaActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : AppCompatActivity() {
    //variaveis quue serão utilizadas
    private lateinit var email: String
    private lateinit var senha: String
    private lateinit var loginViewModel: UserViewModel //variavel para usar os metodos da viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"

        //tela de recuperacao de senha
        txtViewEsqueciSenha.setOnClickListener {
            //Quando o "esqueci a senha" for clicado abre a activity RecuperarSenhaActivity
            startActivity(Intent(this, RecuperarSenhaActivity::class.java))
        }

        //setando botao entrar
        btLogin.setOnClickListener {
            //quando clicado inicialmente não se tem erro
            loginUserLayoutInpTextEmail.error = null
            loginUserLayoutInpTextSenha.error = null

            it.hideKeyboard()

            //Faz a validaçao do email e senha
            if (validaEmail() && validaSenha()) {
                fazerLogin()
            }
        }
    }

    private fun fazerLogin() {
        //Coletando dados do usuario
        doAsync {
            loginViewModel = UserViewModel(AppDatabase.getDatabase(this@LoginActivity))
            val usuario = loginViewModel.consultarLoginExistente(email)
            try {
                Log.d("email", usuario.email)
                Log.d("senha", usuario.senha)

                fazerLogin(usuario)

                // Quando o e-mail digitado não está cadastrado aparece uma mensagem de erro
            } catch (ex: Exception) {
                Log.d("FAZ.LOG", ex.toString())
                uiThread {
                    loginUserLayoutInpTextEmail.error = "E-mail não cadastrado!"
                }
            }
        }
    }

    private fun AnkoAsyncContext<LoginActivity>.fazerLogin(usuario: Usuario) {
        //Se a senha digitada não for válida, aparece uma mensagem de erro
        if (usuario.senha != senha) {
            uiThread {
                loginUserLayoutInpTextSenha.error = "Senha incorreta, por favor verifique!"
            }
        } else {
            //Se a senha for válida vai para a próxima activity, a HomeActivity
            uiThread {
                escreverNoSharedPreferences(usuario)
                Toast.makeText(this@LoginActivity, "Login com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finishAffinity()
            }
        }
    }

    //esconde teclado
    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    //Valida o email
    private fun validaEmail(): Boolean {
        email = txtEmailLogin.text.toString()
        //Se não houver um e-mail digitado, aparece uma mensagem de erro
        if (email.isEmpty()) {
            loginUserLayoutInpTextEmail.error = "Por favor insira um e-mail!"
            return false
            //Se o e-mail digitado não for válido, aparece uma mensagem de erro
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginUserLayoutInpTextEmail.error = "E-mail inválido!"
            return false
        }
        return true
    }

    //Validação de Senha
    private fun validaSenha(): Boolean {
        senha = txtSenhaLogin.text.toString()
        //Se não houver uma senha digitada, aparece uma mensagem de erro
        if (senha.isEmpty()) {
            loginUserLayoutInpTextSenha.error = "Insira uma senha!"
            return false
        }
        return true
    }

    //armazena informações que foram logadas
    private fun escreverNoSharedPreferences(usuario: Usuario) {
        if (materialCheckBox.isChecked){
            val usuarioJson = Gson().toJson(usuario)
            val usuarioNomeJson = Gson().toJson(usuario.nomeUsuario)
            val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
            val sharedEditor = sharedPreferences.edit()
            sharedEditor.putString("login", usuarioJson)
            sharedEditor.putString("nome", usuarioNomeJson)
            sharedEditor.putString("email_usuario", txtEmailLogin.text.toString())
            sharedEditor.apply()
        }
    }
}