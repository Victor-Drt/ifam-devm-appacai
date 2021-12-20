package com.ifam.devm.appacai.ui.funcionarios

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_editar_produto.*
import kotlinx.android.synthetic.main.activity_visualizar_funcionario.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*

class VisualizarFuncionarioActivity : AppCompatActivity() {
    private lateinit var funcionario: Funcionario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_funcionario)

        toolbarVisualizarFuncionario.setOnClickListener {
            onBackPressed()
            finish()
        }

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
        if (funcionario?.foto != null) {
            var fotofuncionario = BitmapFactory.decodeByteArray(funcionario.foto, 0, (funcionario.foto)?.size!!)
            imageFuncionarioVisu?.setImageBitmap(fotofuncionario)
        }
        TextInputNome.setText(funcionario.nome_funcionario)
        textInputEmail.setText(funcionario.email_funcionario)
        textInputTelefone.setText(funcionario.telefone_funcionario)
        TextInputCPF.setText(funcionario.cpf_funcionario)
        TextInputMetaVendas.setText(funcionario.meta_vendas.toString())
        TextInputTotalVendas.setText(funcionario.total_vendas.toString())

        determinateBar.progress = ((funcionario.total_vendas * 100) / funcionario.meta_vendas).toInt()
    }

}