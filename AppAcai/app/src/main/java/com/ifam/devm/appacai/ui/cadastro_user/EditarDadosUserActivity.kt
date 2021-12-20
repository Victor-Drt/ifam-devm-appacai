package com.ifam.devm.appacai.ui.cadastro_user

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UserViewModel
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoViewModel
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import com.ifam.devm.appacai.ui.home.AdminFragment
import com.ifam.devm.appacai.ui.startup.SplashActivity
import kotlinx.android.synthetic.main.activity_editar_dados_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.regex.Matcher

class EditarDadosUserActivity : AppCompatActivity() {

    private lateinit var nome: String
    private lateinit var nomeEmpresa: String
    private lateinit var email: String
    private lateinit var pix: String
    private lateinit var senha: String

    private lateinit var viewModel: UserViewModel
    private lateinit var usuario: Usuario

    private lateinit var prodViewModel: CadastrarProdutoViewModel
    private lateinit var prod: Produto

    private lateinit var funcViewModel: CadastrarFuncionarioViewModel
    private lateinit var func: Funcionario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_dados_user)

        //seta da action bar
        toolbarDadosLojaEditar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        //carrega dados do banco no edit text
        doAsync {
            viewModel = UserViewModel(AppDatabase.getDatabase(this@EditarDadosUserActivity))
            viewModel.CarregaDadosEDITAR()
            uiThread {
                usuario = viewModel.pegaDadosUsuario()
                txtNomeEditar.setText(usuario.nomeUsuario)
                txtNomeFantasiaEditar.setText(usuario.nomeEmpresa)
                txtEmailEditar.setText(usuario.email)
                txtPixEditar.setText(usuario.chavePix)
            }
        }

        // botao salvar
        btSalvarEdit.setOnClickListener {
            if (validarDados() && verificarSenha(txtSenhaEditar.text.toString())) {
                showToast()
                usuario.nomeUsuario = txtNomeEditar.text.toString()
                usuario.nomeEmpresa = txtNomeFantasiaEditar.text.toString()
                usuario.email = txtEmailEditar.text.toString()
                usuario.chavePix = txtPixEditar.text.toString()
                doAsync {
//                    atualiza os dados
                    viewModel.atualizarDados(usuario)
                }
                onBackPressed()
                finish()
            }
        }

//        botao excluir
        btExcluirEdit.setOnClickListener {
            if (verificarSenha(txtSenhaEditar.text.toString())) {
                showDialog()
            }
        }
    }

    private fun validarDados(): Boolean {
        nome = txtNomeEditar.text.toString()
        nomeEmpresa = txtNomeFantasiaEditar.text.toString()
        email = txtEmailEditar.text.toString()
        pix = txtPixEditar.text.toString()
        senha = txtSenhaEditar.text.toString()

//        verifica se os campos estao vazios
        if (nome.isEmpty()) {
            editUserLayoutInputTextUserName.error = "Nome não pode ser vazio!"
            return false
        }
        editUserLayoutInputTextUserName.error = null

        if (nomeEmpresa.isEmpty()) {
            editUserLayoutInputTextEmpresa.error = "Nome Fantasia não pode ser vazio!"
            return false
        }
        editUserLayoutInputTextEmpresa.error = null

        if (email.isEmpty() && (!Patterns.EMAIL_ADDRESS.matcher(email))) {
            editUserLayoutInputTextEmail.error = "E-mail não pode ser vazio!!"
            return false
        }

        if (pix.isEmpty()) {
            editUserLayoutInputTextPix.error = "Chave Pix não pode estar vazio!"
            return false
        }
        editUserLayoutInputTextPix.error = null

        if (senha.isEmpty()) {
            editserLayoutInputTextSenha.error = "Insira uma senha!"
            return false
        }
        editserLayoutInputTextSenha.error = null

        return true
    }

    private fun showToast() {
        Toast.makeText(
            applicationContext,
            "Dados Atualizados Com Sucesso!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showDialog() {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(R.string.text_message_deleteAdmin)
                setPositiveButton(R.string.text_dialog_positive,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        doAsync {
                            prodViewModel = CadastrarProdutoViewModel(AppDatabase.getDatabase(this@EditarDadosUserActivity))
                            funcViewModel = CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@EditarDadosUserActivity))
                            prodViewModel.deleteAllProdutos()
                            funcViewModel.deleteAllFuncionarios()
                            viewModel.removerDados(usuario) // exclui o usuario
                        }
                        val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
                        val sharedEditor = sharedPreferences.edit()
                        sharedEditor.putString("login", "")
                        sharedEditor.apply()
                        //nova activity
                        startActivity(Intent(this@EditarDadosUserActivity, SplashActivity::class.java))
                        finishAffinity()
                    })
                setNegativeButton(R.string.text_dialog_cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        dialog.cancel()
                    })
            }

            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

    private fun verificarSenha(senha: String): Boolean {
        if (senha != usuario.senha) {
            editserLayoutInputTextSenha.error = "Senha incorreta!"
            return false
        } else {
            editserLayoutInputTextSenha.error = null
            return true
        }
    }
}

private operator fun Matcher.not(): Boolean {
    return true
}