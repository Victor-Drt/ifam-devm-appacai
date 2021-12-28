package com.ifam.devm.appacai.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_FUNC
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_produto.AvaliarProdutosActivity
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.startup.SplashActivity
import com.ifam.devm.appacai.ui.venda.SelecaoProdutosActivity
import kotlinx.android.synthetic.main.activity_vendedor_main.*
import kotlinx.android.synthetic.main.activity_visualizar_funcionario.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class VendedorMainActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastrarFuncionarioViewModel
    private lateinit var funcionario: Funcionario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendedor_main)

        btnLogoffVendedor.setOnClickListener {
            val sharedPreferences =
                this.getSharedPreferences(PREF_DATA_FUNC, MODE_PRIVATE)
            val sharedEditor = sharedPreferences?.edit()
            sharedEditor?.putString("login_func", "")
            sharedEditor?.apply()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        cardViewNovoPedido.setOnClickListener {
            val intent = Intent(this, SelecaoProdutosActivity::class.java)
            intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
            println(funcionario.nome_funcionario)
            startActivity(intent)
        }

        cardViewAvaliarProduto.setOnClickListener {
            val intent = Intent(this, AvaliarProdutosActivity::class.java)
            intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
            startActivity(intent)
        }
    }

    override fun onStart() {
        carregaDadosDoBanco()
        super.onStart()
    }

    private fun carregaDadosDoBanco() {
        try {
            val intent = intent
            val nomeFuncPrefs = intent.getStringExtra("funcionario_nome")

            println(nomeFuncPrefs)

            doAsync {
                viewModel =
                    CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VendedorMainActivity))
                funcionario = viewModel.consultaFuncionarioPorNome(nomeFuncPrefs!!)

                println(nomeFuncPrefs)
                println(funcionario.nome_funcionario)

                uiThread {
                    textValorArrecadadoMain.setText((funcionario.total_vendas).toString())
                    progressBarVendedor.progress =
                        ((funcionario.total_vendas * 100) / funcionario.meta_vendas).toInt()
                }
            }
        } catch (e : Exception) {
            Log.d("Erro Manter Sessao Funcionario!", "Nao foi possivel pegar usuario por intent!")
            try {
                val sharedPreferences =
                    this.getSharedPreferences(PREF_DATA_FUNC, MODE_PRIVATE)
                val nome = sharedPreferences.getString("nome_func", "Sem nome")

                println(nome)

                doAsync {
                    viewModel =
                        CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VendedorMainActivity))
                    funcionario = viewModel.consultaFuncionarioPorNome(nome!!)

                    println(funcionario.nome_funcionario)

                    uiThread {
                        textValorArrecadadoMain.setText((funcionario.total_vendas).toString())
                        progressBarVendedor.progress =
                            ((funcionario.total_vendas * 100) / funcionario.meta_vendas).toInt()
                    }
                }

            } catch (e : Exception) {
                Log.d("Erro Manter Sessao Funcionario!", "Nao foi possivel pegar usuario por shared preference!")
            }
        }
    }

}