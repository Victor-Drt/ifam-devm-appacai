package com.ifam.devm.appacai.ui.cadastro_produto

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class VisualizarProdutoActivity : AppCompatActivity() {
    //    produto
    private lateinit var produto: Produto
    private lateinit var prodViewModel : CadastrarProdutoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_produto)

        toolbarVisualizarProduto.setOnClickListener {
            onBackPressed()
            finish()
        }

        toolbarVisualizarProduto.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editar_dados -> {
                    //cria uma intent enviando as informaçoes do produto para a prox tela
                    val intent = Intent(this, EditarProdutoActivity::class.java)
                    intent.putExtra("produto_nome", produto.nome)
                    startActivity(intent)
                }
            }
            true
        }

    }

    //coleta os dados no banco
    override fun onStart() {
        carregaDadosDoBanco()
        super.onStart()
    }

    private fun carregaDadosDoBanco() {
        val intent = intent
        val produtoNome = intent.getStringExtra("produto_nome")

        doAsync {
            prodViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@VisualizarProdutoActivity))
            produto = prodViewModel.consultarProdutoExistente(produtoNome.toString())

            println("Produto ${produto.nome}")
            println("Frequencia vendas ${produto.freqVenda}")

            uiThread {
                if (produto?.foto != null) {
                    var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
                    imageProdutoVisu?.setImageBitmap(fotoproduto)
                }
                txtNomeVerProduto.setText(produto.nome)
                txtDescricaoVerProduto.setText(produto.descricao)
                txtValorVerProduto.setText((produto.valor).toString())
                txtTipoVerProduto.setText(produto.tipo)
                ratingBarProdutos.rating = (produto.avaliacao)/produto.qtdVotos
            }
        }
    }
}