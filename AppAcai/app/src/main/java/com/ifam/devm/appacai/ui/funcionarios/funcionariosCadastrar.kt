package com.ifam.devm.appacai.ui.funcionarios

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import org.jetbrains.anko.doAsync

class funcionariosCadastrar: AppCompatActivity() {
    private lateinit var nome: String;
    private lateinit var email:String;
    private lateinit var telefone: String;
    private lateinit var  cpf: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_cadastra_funcionario)

        setNullFields()

        btn_cadastrar.setOnClickListener {
            if(validarDados()){
                val novoFuncionario = Funcionario (
                    1,
                    textNomeInput.text.toString(),
                    textEmailInput.text.toString(),
                    textTelefoneInput.text.toString(),
                    textCpfInput.text.toString()
                )

                val db = AppDatabase.getDatabase(this)
                doAsync {

                }
            }
        }


    }

    private fun validarDados(): Boolean {
        nome = textNomeInput.text.toString()
        email = textEmailInput.text.toString()
        telefone = textTelefoneInput.text.toString()
        cpf = textCpfInput.text.toString()

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

        return true
    }

    fun setNullFields(){
        cadInputNome.error = null
        cadInputEmail.error = null
        cadInputCpf.error = null
        cadInputTelefone.error = null
    }
}