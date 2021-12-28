package com.ifam.devm.appacai.ui.funcionarios

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_editar_produto.*
import kotlinx.android.synthetic.main.activity_visualizar_funcionario.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class VisualizarFuncionarioActivity : AppCompatActivity() {
    private lateinit var funcionario: Funcionario
    private lateinit var funcViewModel: CadastrarFuncionarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_funcionario)

        toolbarVisualizarFuncionario.setOnClickListener {
            onBackPressed()
            finish()
        }

        toolbarVisualizarFuncionario.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editar_dados -> {
                    val intent = Intent(this, EditarFuncionarioActivity::class.java)
                    intent.putExtra("funcionario_nome", TextInputNome.text.toString())
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onStart() {
        carregaDadosDoBanco()
        super.onStart()
    }

    private fun carregaDadosDoBanco() {
        val intent = intent
        val funcionarioNome = intent.getStringExtra("funcionario_nome")

        doAsync {
            funcViewModel =
                CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VisualizarFuncionarioActivity))
            funcionario = funcViewModel.consultaFuncionarioPorNome(funcionarioNome.toString())

            println("Funcionario ${funcionario.nome_funcionario}")

            uiThread {
                if (funcionario?.foto != null) {
                    var fotofuncionario = BitmapFactory.decodeByteArray(
                        funcionario.foto,
                        0,
                        (funcionario.foto)?.size!!
                    )
                    imageFuncionarioVisu?.setImageBitmap(fotofuncionario)
                }
                TextInputNome.setText(funcionario.nome_funcionario)
                textInputEmail.setText(funcionario.email_funcionario)
                textInputTelefone.setText(funcionario.telefone_funcionario)
                TextInputCPF.setText(funcionario.cpf_funcionario)
                TextInputMetaVendas.setText(funcionario.meta_vendas.toString())
                TextInputTotalVendas.setText(funcionario.total_vendas.toString())

                determinateBar.progress =
                    ((funcionario.total_vendas * 100) / funcionario.meta_vendas).toInt()
            }
        }


    }

}