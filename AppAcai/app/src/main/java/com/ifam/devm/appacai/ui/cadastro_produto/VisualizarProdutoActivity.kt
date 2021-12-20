package com.ifam.devm.appacai.ui.cadastro_produto

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import kotlinx.android.synthetic.main.activity_visualizar_produto.*

class VisualizarProdutoActivity : AppCompatActivity() {
    //    produto
    private lateinit var produto: Produto

    //    atributos
    private var nome: String = ""
    private var descricao: String = ""
    private var tipo: String = ""
    private var valor: String = ""
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
                    val produtoJson = Gson().toJson(produto)
                    val intentEditarFuncionario =
                        Intent(this, EditarProdutoActivity::class.java)
                    intentEditarFuncionario.putExtra("produto", produtoJson)
                    startActivity(intentEditarFuncionario)
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
        val produtoJson = intent.getStringExtra("produto")
        val produto2 = Gson().fromJson(produtoJson, Produto::class.java)
        produto = produto2
        if (produto?.foto != null) {
            var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
            imageProdutoVisu?.setImageBitmap(fotoproduto)
        }
        txtNomeVerProduto.setText(produto.nome)
        txtDescricaoVerProduto.setText(produto.descricao)
        txtValorVerProduto.setText((produto.valor).toString())
        txtTipoVerProduto.setText(produto.tipo)
        ratingBarProdutos.rating = produto.avaliacao
    }
}