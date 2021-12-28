package com.ifam.devm.appacai.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UserViewModel
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_FUNC
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.home.HomeActivity
import com.ifam.devm.appacai.ui.home.VendedorMainActivity
import com.ifam.devm.appacai.ui.senha.RecuperarSenhaActivity
import kotlinx.android.synthetic.main.activity_visualizar_funcionario.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginFragment : Fragment() {
    //variaveis quue serão utilizadas
    private lateinit var email: String
    private lateinit var senha: String
    private lateinit var loginViewModel: UserViewModel //variavel para usar os metodos da viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tela de recuperacao de senha
        txtViewEsqueciSenha.setOnClickListener {
            //Quando o "esqueci a senha" for clicado abre a activity RecuperarSenhaActivity
            startActivity(
                Intent(
                    this@LoginFragment.requireContext(),
                    RecuperarSenhaActivity::class.java
                )
            )
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
            loginViewModel =
                UserViewModel(AppDatabase.getDatabase(this@LoginFragment.requireContext()))
            val usuario = loginViewModel.consultarLoginExistente(email)
            try {
                Log.d("email", usuario.email)
                Log.d("senha", usuario.senha)

                fazerLogin(usuario)

                // Quando o e-mail digitado não e de um admin ele pesquisara um email de funcionario
            } catch (ex: Exception) {
                Log.d("FAZ.LOG", ex.toString())
                Log.d("FAZ.LOG", "Procurando email de funcionario")

                loginViewModel =
                    UserViewModel(AppDatabase.getDatabase(this@LoginFragment.requireContext()))
                val funcionario = loginViewModel.consultarLoginExistenteFunc(email)

                try {
                    Log.d("email funcionario", funcionario.email_funcionario)
                    Log.d("senha", funcionario.senha)

                    fazerLoginFunc(funcionario)

                    // Quando o e-mail digitado não está cadastrado aparece uma mensagem de erro
                } catch (ex: Exception) {
                    uiThread {
                        loginUserLayoutInpTextEmail.error = "E-mail não cadastrado!"
                    }
                }
            }
        }
    }

    private fun AnkoAsyncContext<LoginFragment>.fazerLogin(usuario: Usuario) {
        //Se a senha digitada não for válida, aparece uma mensagem de erro
        if (usuario.senha != senha) {
            uiThread {
                loginUserLayoutInpTextSenha.error = "Senha incorreta, por favor verifique!"
            }
        } else {
            //Se a senha for válida vai para a próxima activity, a HomeActivity
            uiThread {
                escreverNoSharedPreferences(usuario)
                Toast.makeText(
                    this@LoginFragment.requireContext(),
                    "Login com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@LoginFragment.requireContext(), HomeActivity::class.java))
                activity?.finish()
            }
        }
    }


    private fun AnkoAsyncContext<LoginFragment>.fazerLoginFunc(funcionario: Funcionario) {
        //Se a senha digitada não for válida, aparece uma mensagem de erro
        if (funcionario.senha != senha) {
            uiThread {
                loginUserLayoutInpTextSenha.error = "Senha incorreta, por favor verifique!"
            }
        } else {
            //Se a senha for válida vai para a próxima activity, a HomeActivity
            uiThread {
                escreverNoSharedPreferencesFunc(funcionario)
                Toast.makeText(
                    this@LoginFragment.requireContext(),
                    "Login de funcionario com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@LoginFragment.requireContext(), VendedorMainActivity::class.java)
                intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
                startActivity(intent)
                activity?.finish()
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
        if (materialCheckBox.isChecked) {
            val usuarioJson = Gson().toJson(usuario)
            val usuarioNomeJson = Gson().toJson(usuario.nomeUsuario)
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val sharedEditor = sharedPreferences?.edit()
            sharedEditor?.putString("login", usuarioJson)
            sharedEditor?.putString("nome", usuarioNomeJson)
            sharedEditor?.putString("email_usuario", txtEmailLogin.text.toString())
            sharedEditor?.apply()
        }
    }

    //armazena informações que foram logadas
    private fun escreverNoSharedPreferencesFunc(funcionario: Funcionario) {
        if (materialCheckBox.isChecked) {
            val funcionarioJson = Gson().toJson(funcionario)
            val funcionarioNomeJson = Gson().toJson(funcionario.nome_funcionario)
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_FUNC,
                AppCompatActivity.MODE_PRIVATE
            )
            val sharedEditor = sharedPreferences?.edit()
            sharedEditor?.putString("login_func", funcionarioJson)
            sharedEditor?.putString("nome_func", funcionarioNomeJson)
            sharedEditor?.putString("email_func", txtEmailLogin.text.toString())
            sharedEditor?.apply()
            println("Nome check ${funcionarioNomeJson}")
        }
    }
}