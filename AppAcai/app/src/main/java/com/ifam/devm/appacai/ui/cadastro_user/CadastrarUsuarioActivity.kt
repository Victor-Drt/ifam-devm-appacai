package com.ifam.devm.appacai.ui.cadastro_user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.ifam.devm.appacai.MainActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import kotlinx.android.synthetic.main.activity_cadastrar_usuario.*
import org.jetbrains.anko.doAsync
import java.util.regex.Matcher

class CadastrarUsuarioActivity : AppCompatActivity() {
    private lateinit var nome: String
    private lateinit var nomeFantasia: String
    private lateinit var email: String
    private lateinit var chavePix: String
    private lateinit var senha: String
    private lateinit var senhaConfirmar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_usuario)

        setNullFields()

//      quando o botão cadastrar for clicado é iniciado o processo abaixo
        btCadastrar.setOnClickListener {
            //instanciando usuario com os dados da textView
            if(validarDados()) {
                val novoCadastro = Usuario(
                    1,
                    txtNomeCadastrar.text.toString(),
                    txtNomeFantasiaCadastrar.text.toString(),
                    txtEmailCadastrar.text.toString(),
                    txtPixCadastrar.text.toString(),
                    txtSenhaCadastrar.text.toString(),
                )

                //comunicando com o database
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.usuarioDao().insert(novoCadastro)
                }
                //inicia outra activity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
}
    private fun validarDados():Boolean {
        // variáveis que recebem as informações inseridas pelo usuário no cadastro
        nome = txtNomeCadastrar.text.toString()
        nomeFantasia = txtNomeFantasiaCadastrar.text.toString()
        email = txtEmailCadastrar.text.toString()
        chavePix = txtPixCadastrar.text.toString()
        senha = txtSenhaCadastrar.text.toString()
        senhaConfirmar = txtConfirmarSenhaCadastrar.text.toString()

        // se o campo de Nome  estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (nome.isEmpty()){
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
        if (chavePix.isEmpty()){
            cadUserLayoutInputTextPix.error = "Insira uma chave pix!"
            return false
        }
        cadUserLayoutInputTextPix.error = null

        // se o campo de Senha estiver vazio, uma mensagem de erro é emitida, senão segue com o cadastro
        if (senha.isEmpty()){
            cadUserLayoutInputTextSenha.error = "Insira uma Senha!"
            return false
        }
        cadUserLayoutInputTextSenha.error = null
        //se o campo de Confirmar a Senha estiver diferente do campo Senha, uma mensagem de erro é emitida
        //Senão segue com o cadastro
        if (senha != senhaConfirmar){
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