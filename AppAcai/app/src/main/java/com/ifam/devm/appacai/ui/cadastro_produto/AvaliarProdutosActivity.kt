package com.ifam.devm.appacai.ui.cadastro_produto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.AvalProdutoAdapter
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.home.VendedorMainActivity
import com.ifam.devm.appacai.ui.venda.ProdutoAvaliadoActivity
import kotlinx.android.synthetic.main.activity_avaliar_produtos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AvaliarProdutosActivity : AppCompatActivity() {
    private lateinit var produtosCadastrados: MutableList<Produto>
    private lateinit var produtosAdapter: AvalProdutoAdapter
    private lateinit var nomeFuncionario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliar_produtos)

        toolbarAvaliarProduto.setNavigationOnClickListener {
            val intent = Intent(this, VendedorMainActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            println("Selecao Produto Acitivty - ${nomeFuncionario}")
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        nomeFuncionario = intent.getStringExtra("funcionario_nome").toString()

//        incializando o RecyclerView
        produtosAdapter = AvalProdutoAdapter(
            this,
            mutableListOf(),
            ::onClickItem,
        )

        rvListaProdutosAval.adapter = produtosAdapter
        rvListaProdutosAval.layoutManager = LinearLayoutManager(this)

//        Inicializa viewModel e obtém lista de produtos
        doAsync {
            val produtosViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@AvaliarProdutosActivity))
            produtosCadastrados = produtosViewModel.getAllProduto() as MutableList<Produto>
            uiThread {
                produtosAdapter.swapData(produtosCadastrados)
            }
        }
    }

    override fun onBackPressed() {
        if (shouldAllowBack()) {
            super.onBackPressed()
        } else {
            val intent = Intent(this, VendedorMainActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            println("Selecao Produto Acitivty - ${nomeFuncionario}")
            startActivity(intent)
            finish()
        }
    }

    private fun shouldAllowBack(): Boolean {
        return false
    }

    private fun onClickItem(produto: Produto) {
        val intent = Intent(this, ProdutoAvaliadoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        intent.putExtra("funcionario_nome", nomeFuncionario)
        startActivity(intent)
        finish()
    }
}