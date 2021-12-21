package com.ifam.devm.appacai.ui.cadastro_user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.startup.StartupActivity
import kotlinx.android.synthetic.main.activity_cadastrar_produto.*
import kotlinx.android.synthetic.main.fragment_cadastrar_usuario.*
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream
import java.util.regex.Matcher

class CadastrarUsuarioFragment : Fragment() {
    private lateinit var fotoQR: ByteArray
    private lateinit var nome: String
    private lateinit var nomeFantasia: String
    private lateinit var email: String
    private lateinit var chavePix: String
    private lateinit var senha: String
    private lateinit var senhaConfirmar: String

    val COD_IMAGE = 101
    var imageBitMap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastrar_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNullFields()

        textCliqueInserirCodigoQr.setOnClickListener {
            abrirGaleria()
        }

        //      quando o botão cadastrar for clicado é iniciado o processo abaixo
        btCadastrar.setOnClickListener {
            //instanciando usuario com os dados da textView
            if (validarDados()) {
                val novoCadastro = Usuario(
                    1,
//                    textCliqueInserirCodigoQr
                    fotoQR,
                    txtNomeCadastrar.text.toString(),
                    txtNomeFantasiaCadastrar.text.toString(),
                    txtEmailCadastrar.text.toString(),
                    txtPixCadastrar.text.toString(),
                    txtSenhaCadastrar.text.toString(),
                )

                //comunicando com o database
                val db = AppDatabase.getDatabase(this@CadastrarUsuarioFragment.requireContext())
                doAsync {
                    db.usuarioDao().insert(novoCadastro)
                }
                //inicia outra activity
                startActivity(
                    Intent(
                        this@CadastrarUsuarioFragment.requireContext(),
                        StartupActivity::class.java
                    )
                )
                onDestroy()
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
                //lendo URI com a imagem
                val inputStream = this@CadastrarUsuarioFragment.requireContext().contentResolver.openInputStream((data.data!!))
                //transformando o resultado em bitmap
                imageBitMap = BitmapFactory.decodeStream(inputStream)
                //exibir a imagem no aplicativo
                imageQRCad.setImageBitmap(imageBitMap)


                var saida: ByteArrayOutputStream = ByteArrayOutputStream()
                imageBitMap?.compress(Bitmap.CompressFormat.PNG, 100, saida)
                fotoQR = saida.toByteArray()
            }
        }
    }

    private fun validarDados(): Boolean {
        // variáveis que recebem as informações inseridas pelo usuário no cadastro
        nome = txtNomeCadastrar.text.toString()
        nomeFantasia = txtNomeFantasiaCadastrar.text.toString()
        email = txtEmailCadastrar.text.toString()
        chavePix = txtPixCadastrar.text.toString()
        senha = txtSenhaCadastrar.text.toString()
        senhaConfirmar = txtConfirmarSenhaCadastrar.text.toString()

        // se o campo de Nome  estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (nome.isEmpty()) {
            cadUserLayoutInputTextUserName.error = "Insira um Nome!"
            return false
        }
        cadUserLayoutInputTextUserName.error = null
        // se o campo de nome fantasia estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (nomeFantasia.isEmpty()) {
            cadUserLayoutInputTextEmpresa.error = "Insira um nome fantasia"
            return false
        }
        cadUserLayoutInputTextEmpresa.error = null

        // se o campo de Email estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (email.isEmpty()) {
            cadUserLayoutInputTextEmail.error = "Insira um e-mail!"
            return false
            // se o Email não tiver formato válido, uma mensagem de erro é emitida, senão segue com o cadastro
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            cadUserLayoutInputTextEmail.error = "E-mail inválido!"
            return false
        }
        cadUserLayoutInputTextEmail.error = null

        // se o campo de chave pix estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (chavePix.isEmpty()) {
            cadUserLayoutInputTextPix.error = "Insira uma chave pix!"
            return false
        }
        cadUserLayoutInputTextPix.error = null

        // se o campo de Senha estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (senha.isEmpty()) {
            cadUserLayoutInputTextSenha.error = "Insira uma Senha!"
            return false
        }
        cadUserLayoutInputTextSenha.error = null
        //se o campo de Confirmar a Senha estiver diferente do campo Senha, uma mensagem de erro é emitida
        //Senão segue com o cadastro
        if (senha != senhaConfirmar) {
            cadUserLayoutInputTextConfirmaSenha.error = "Senhas não correspondem!"
            return false
        }
        cadUserLayoutInputTextConfirmaSenha.error = null
        return true
    }

    fun setNullFields() {
        //setando os layout input texts para null
        //nenhuma mensagem de erro é emitida inicialmente
        cadUserLayoutInputTextUserName.error = null
        cadUserLayoutInputTextEmpresa.error = null
        cadUserLayoutInputTextEmail.error = null
        cadUserLayoutInputTextPix.error = null
        cadUserLayoutInputTextSenha.error = null
        cadUserLayoutInputTextConfirmaSenha.error = null
    }
}

//função para forçar um retorno de true na mask e partner
private operator fun Matcher.not(): Boolean {
    return true
}