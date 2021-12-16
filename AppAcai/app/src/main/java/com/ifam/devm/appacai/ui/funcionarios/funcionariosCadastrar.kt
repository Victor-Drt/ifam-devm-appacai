package com.ifam.devm.appacai.ui.funcionarios

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.home.HomeActivity
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.fragment_vendedores.*
import org.jetbrains.anko.doAsync

class funcionariosCadastrar: AppCompatActivity() {
    private lateinit var nome: String;
    private lateinit var email:String;
    private lateinit var telefone: String;
    private lateinit var  cpf: String;
    private lateinit var  totalVendas: String;
    private lateinit var metaVendas: String;
    private lateinit var senha: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_vendedores)

        setNullFields()

        btn_cadastrar.setOnClickListener {
            if(validarDados()){
                val novoFuncionario = Funcionario (
                    1,
                    textNomeInput.text.toString(),
                    textEmailInput.text.toString(),
                    textTelefoneInput.text.toString(),
                    textCpfInput.text.toString(),
                    textMetaVendasInput.text.toString() as Double,
                    textInputTotalVendas.text.toString() as Double,
                    textInputSenha.text.toString()
                )
                Toast.makeText(this, "Teste eee", Toast.LENGTH_LONG).show()
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.funcionarioDao().insert(novoFuncionario)

                }
                Toast.makeText(this, "Teste", Toast.LENGTH_LONG).show()
                //inicia outra activity
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }


    }

    private fun validarDados(): Boolean {
        nome = textNomeInput.text.toString()
        email = textEmailInput.text.toString()
        telefone = textTelefoneInput.text.toString()
        cpf = textCpfInput.text.toString()
        totalVendas = textInputTotalVendas.text.toString()
        metaVendas = textMetaVendasInput.text.toString()
        senha = textInputSenha.text.toString()

        if(nome.isEmpty()){
            cadInputNome.error = "Insira um nome!"
            return false
        }
        cadInputNome.error = null

        if(email.isEmpty()){
            cadInputEmail.error = "Insira um e-mail!"
            return false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            cadInputEmail.error = "E-mail inválido!"
            return false
        }
        cadInputEmail.error = null

        if(cpf.isEmpty()){
            cadInputCpf.error = "Insira um CPF!"
            return false
        }
        cadInputCpf.error = null

        if(totalVendas.isEmpty()){
            cadInputTotalVendas.error = "Insira um total de vendas"
            return false
        }

        cadInputTotalVendas.error = null

        if(metaVendas.isEmpty()){
            cadInputMetaVendas.error = "Insira uma meta de vendas"
            return false
        }

        cadInputMetaVendas.error = null

        if(senha.isEmpty()){
            cadInputSenha.error = "Insira uma senha!"
            return false
        }

        cadInputSenha.error = null

        return true
    }

    fun setNullFields(){
        cadInputNome.error = null
        cadInputEmail.error = null
        cadInputCpf.error = null
        cadInputTelefone.error = null
    }
}