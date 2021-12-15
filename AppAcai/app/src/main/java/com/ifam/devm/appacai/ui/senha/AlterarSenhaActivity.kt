package com.ifam.devm.appacai.ui.senha

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UserViewModel
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.startup.SplashActivity
import kotlinx.android.synthetic.main.activity_alterar_senha.*
import kotlinx.android.synthetic.main.activity_cadastrar_usuario.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AlterarSenhaActivity : AppCompatActivity() {
    //Variaveis a serem utilizadas
    private lateinit var senha: String
    private lateinit var confSenha: String
    private lateinit var viewModel: UserViewModel //var para usar os metodos da viewModel
    private lateinit var usuario: Usuario //var de usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)

        //seta da action bar
        toolbarDadosLojaRecSenha.setNavigationOnClickListener {
            startActivity(Intent(this@AlterarSenhaActivity, SplashActivity::class.java))
        }

        btAlterarSenha.setOnClickListener {
            //seta os input layouts como null
            updateSenhaUsuarioLayoutInpTextSenha.error = null
            updateSenhaUsuarioLayoutInpTextSenhaConfirmar.error = null

            //coleta dados do banco
            doAsync {
                viewModel = UserViewModel(AppDatabase.getDatabase(this@AlterarSenhaActivity))
                viewModel.carregaDadosALTERAR()
                uiThread {
                    usuario = viewModel.getTodosDadosUsuario()

                    //faz a validação
                    if (validaSenha() && validaConfSenha()) {
                        Toast.makeText(
                            applicationContext,
                            "Senha atualizada com sucesso",
                            Toast.LENGTH_LONG
                        ).show()
                        usuario.senha = txtSenhaAtualizar.text.toString()
                        doAsync {
                            //atualiza a senha
                            viewModel.atualizaSenha(usuario)
                        }
                        startActivity(Intent(this@AlterarSenhaActivity, SplashActivity::class.java))
                        finish()
                    }
                }

            }
        }
    }

    //Validaçao de senha
    private fun validaSenha(): Boolean {
        senha = txtSenhaAtualizar.text.toString()
        if (senha.isEmpty()) {
            updateSenhaUsuarioLayoutInpTextSenha.error = "Insira uma senha!"
            return false
        }
        return true
    }
    //Confirmar igualdade de senhas
    private fun validaConfSenha(): Boolean {
        confSenha = txtConfirmarSenhaAtualizar.text.toString()
        if (confSenha.isEmpty()) {
            updateSenhaUsuarioLayoutInpTextSenhaConfirmar.error = "Insira uma senha!"
            return false
        } else if (confSenha != senha) {
            updateSenhaUsuarioLayoutInpTextSenhaConfirmar.error = "As senhas não são compatíveis!"
            return false
        }
        return true
    }
}