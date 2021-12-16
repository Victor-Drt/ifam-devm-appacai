package com.ifam.devm.appacai.ui.funcionarios

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_visualizar_funcionario.*

class VisualizarFuncionarioActivity : AppCompatActivity() {
    private lateinit var funcionario: Funcionario

    //Atributos
    private var nome: String = ""
    private var email: String = ""
    private  var telefone: String = ""
    private var cpf: String = ""
    private var metaVenda: Double = 0.0
    private var totalVenda: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_funcionario)

        toolbarVisualizarFuncionario.setOnMenuItemClickListener{ item ->
            when (item.itemId) {
                R.id.editar_dados -> {
                    val funcionarioJson = Gson().toJson(funcionario)
                    val intentEditarFuncionario = Intent(this, EditarFuncionarioActivity::class.java)
                    intentEditarFuncionario.putExtra("funcionario", funcionarioJson)
                    startActivity(intentEditarFuncionario)
                }
            }
            true
        }
    }

    override fun onStart() {
        carregaDadosDoBanco()
        super.onStart()
    }

    private fun carregaDadosDoBanco(){
        val funcionarioJson = intent.getStringExtra("funcionario")
        val funcionario2 = Gson().fromJson(funcionarioJson, Funcionario::class.java)
        funcionario = funcionario2
        textNomeInput.setText(funcionario.nome_funcionario)
        textEmailInput.setText(funcionario.email_funcionario)
        textTelefoneInput.setText(funcionario.telefone_funcionario)
        textCpfInput.setText(funcionario.cpf_funcionario)
        textMetaVendasInput.setText(funcionario.meta_vendas as String)
        textInputTotalVendas.setText(funcionario.total_vendas as String)
    }

}