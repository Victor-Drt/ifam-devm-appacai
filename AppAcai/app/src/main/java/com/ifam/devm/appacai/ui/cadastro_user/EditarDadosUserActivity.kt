package com.ifam.devm.appacai.ui.cadastro_user

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.ifam.devm.appacai.utils.Mask
import com.ifam.devm.appacai.utils.MaskCopy.MaskChangedListener
import com.ifam.devm.appacai.utils.MaskCopy.MaskSL
import com.ifam.devm.appacai.utils.MaskCopy.MaskStyle
import kotlinx.android.synthetic.main.activity_editar_dados_user.*
import kotlinx.android.synthetic.main.activity_editar_funcionario.*
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
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

    val COD_IMAGE = 101
    var imageBitMap: Bitmap? = null
    var fotoFinal: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_dados_user)

        //seta da action bar
        toolbarDadosLojaEditar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        textCliqueInserirCodigoQrEdit.setOnClickListener {
            abrirGaleria()
        }


        //carrega dados do banco no edit text
        doAsync {
            val intent = intent
            val userNome = intent.getStringExtra("user_name")

            viewModel = UserViewModel(AppDatabase.getDatabase(this@EditarDadosUserActivity))
            viewModel.CarregaDadosEDITAR()
            uiThread {
                usuario = viewModel.pegaDadosUsuario()
                if (usuario?.foto != null) {
                    var fotofuncionario = BitmapFactory.decodeByteArray(usuario.foto, 0, (usuario.foto)?.size!!)
                    imageQREdit?.setImageBitmap(fotofuncionario)
                }
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

                if (fotoFinal == null) {
                    println("foto Vazia")
                } else {
                    usuario.foto = fotoFinal
                }

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

    private fun abrirGaleria() {
        //definindo uma intent para acao de conteudo
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        //definindo filtro para imagens
        intent.type = "image/*"

        //inicializando a activity com o resultado
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), COD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == COD_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    //lendo URI com a imagem
                    val inputStream = contentResolver.openInputStream((data.data!!))
                    //transformando o resultado em bitmap
                    imageBitMap = BitmapFactory.decodeStream(inputStream)
                    //exibir a imagem no aplicativo
                    imageQREdit.setImageBitmap(imageBitMap)

                    //alterando pra salvar no banco
                    var saida: ByteArrayOutputStream = ByteArrayOutputStream()
                    imageBitMap?.compress(Bitmap.CompressFormat.PNG, 100, saida)
                    fotoFinal = saida.toByteArray()
                } catch (e : Exception) {
                    if (usuario?.foto != null) {
                        var fotoUser = BitmapFactory.decodeByteArray(usuario.foto, 0, (usuario.foto)?.size!!)
                        imageQREdit?.setImageBitmap(fotoUser)
                    }
                }
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
                        val db = AppDatabase.getDatabase(this@EditarDadosUserActivity)
                        doAsync {
                            prodViewModel = CadastrarProdutoViewModel(AppDatabase.getDatabase(this@EditarDadosUserActivity))
                            funcViewModel = CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@EditarDadosUserActivity))

                            prodViewModel.deleteAllProdutos()
                            funcViewModel.deleteAllFuncionarios()
                            db.pagamentoDao().deleteAll()
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