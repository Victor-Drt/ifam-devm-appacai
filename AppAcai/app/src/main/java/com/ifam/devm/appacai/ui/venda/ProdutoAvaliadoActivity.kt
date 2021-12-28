package com.ifam.devm.appacai.ui.venda

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.cadastro_produto.AvaliarProdutosActivity
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoViewModel
import com.ifam.devm.appacai.ui.home.VendedorMainActivity
import kotlinx.android.synthetic.main.activity_produto_avaliado.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ProdutoAvaliadoActivity : AppCompatActivity() {
    //    produto
    private lateinit var produto: Produto
    private lateinit var prodViewModel: CadastrarProdutoViewModel
    private lateinit var nomeFuncionario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto_avaliado)

        toolbarProdutoAvaliado.setNavigationOnClickListener {
            val intent = Intent(this, AvaliarProdutosActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            println("Selecao Produto Acitivty - ${nomeFuncionario}")
            startActivity(intent)
            finish()
        }

        btnEnviarAvaliacao.setOnClickListener {
            var nota = txtAvalProdutoNota.text
            if (!nota.isNullOrEmpty() && nota.toString().toFloat() >= 0 && nota.toString().toFloat() <= 5) {
                produto.qtdVotos += 1
                produto.avaliacao += (txtAvalProdutoNota.text.toString()
                    .toFloat())

                println("QTD : ${produto.qtdVotos}")
                println("AVAL : ${produto.avaliacao}")
//                Atualiza os dados no banco
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.produtoDao().update(produto)
                }
                val intent = Intent(this, AvaliarProdutosActivity::class.java)
                intent.putExtra("funcionario_nome", nomeFuncionario)
                println("Selecao Produto Acitivty - ${nomeFuncionario}")
                startActivity(intent)
                finish()
            } else {
                layoutInputAvalProdutoNota.error = "Por favor insira um valor entre 0 e 5!"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nomeFuncionario = intent.getStringExtra("funcionario_nome").toString()
        val produtoNome = intent.getStringExtra("produto_nome")
        doAsync {
            prodViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@ProdutoAvaliadoActivity))
            produto = prodViewModel.consultarProdutoExistente(produtoNome.toString())

            uiThread {
                if (produto?.foto != null) {
                    var fotoproduto =
                        BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
                    imageProdutoVisuAval?.setImageBitmap(fotoproduto)
                }
                txtNomeAvalProduto.setText(produto.nome)
                txtValorAvalProduto.setText(produto.valor.toString())
                ratingBarProdutosAval.rating = (produto.avaliacao)/produto.qtdVotos
            }
        }
    }

    override fun onBackPressed() {
        if (shouldAllowBack()) {
            super.onBackPressed()
        } else {
            val intent = Intent(this, AvaliarProdutosActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            println("Selecao Produto Acitivty - ${nomeFuncionario}")
            startActivity(intent)
            finish()
        }
    }

    private fun shouldAllowBack(): Boolean {
        return false
    }
}