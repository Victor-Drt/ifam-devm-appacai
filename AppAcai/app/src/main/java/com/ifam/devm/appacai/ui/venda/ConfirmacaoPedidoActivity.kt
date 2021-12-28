package com.ifam.devm.appacai.ui.venda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.CarrinhoAdapter
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.ProdutoVenda
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.home.VendedorMainActivity
import kotlinx.android.synthetic.main.activity_confirmacao_pedido.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class ConfirmacaoPedidoActivity : AppCompatActivity() {
    private lateinit var listaProdutosSelecionados: MutableList<ProdutoVenda>
    private lateinit var carrinhoAdapter: CarrinhoAdapter
    private lateinit var nomeFuncionario: String
    private lateinit var viewModel: SelecionarProdutoViewModel
    private var totalPedido = 0.00f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacao_pedido)

        toolbarConfirmaPedido.setNavigationOnClickListener {
            finish()
        }

        btConfirmarPedido.setOnClickListener {
            if (totalPedido > 0) {
//                envia os dados pra proxima tela
                val intent = Intent(this, RealizarPagamentoActivity::class.java)
                intent.putExtra("funcionario_nome", nomeFuncionario)
                intent.putExtra("totalPedido", totalPedido)
                println(nomeFuncionario)
                startActivity(intent)

            } else {
                Toast.makeText(
                    this@ConfirmacaoPedidoActivity,
                    "Sem itens no carrinho!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        nomeFuncionario = intent.getStringExtra("funcionario_nome").toString()
    }

    override fun onResume() {
        super.onResume()
        nomeFuncionario = intent.getStringExtra("funcionario_nome").toString()

        val db = AppDatabase.getDatabase(this@ConfirmacaoPedidoActivity)

        doAsync {
            totalPedido = (db.produtoVendaDao().getProdutoVendaValor()).sum()

            uiThread {
                var totalPedidoString = totalPedido.toString()
                if ((totalPedido.toString().length - totalPedido.toString().indexOf('.')) < 3)
                    totalPedidoString += "0"
                textValorTotal.text = totalPedidoString.replace(".", ",")
            }
        }

        carrinhoAdapter = CarrinhoAdapter(
            this,
            mutableListOf(),
            ::removeItem
        )

        rvListaConfirmProd.adapter = carrinhoAdapter
        rvListaConfirmProd.layoutManager = LinearLayoutManager(this)

        doAsync {
            viewModel =
                SelecionarProdutoViewModel(AppDatabase.getDatabase((this@ConfirmacaoPedidoActivity)))

            listaProdutosSelecionados = viewModel.getAllProdutoVenda() as MutableList<ProdutoVenda>

            uiThread {
                carrinhoAdapter.swapData(listaProdutosSelecionados)
            }
        }
    }

    override fun onBackPressed() {
        if (shouldAllowBack()) {
            super.onBackPressed()
        } else {
            val intent = Intent(this, SelecaoProdutosActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            startActivity(intent)
            println("Confirmacao Pedido Acitivty - ${nomeFuncionario}")
        }
    }

    private fun shouldAllowBack(): Boolean {
        return false
    }

    private fun removeItem(produtoVenda: ProdutoVenda) {
        val db = AppDatabase.getDatabase(this@ConfirmacaoPedidoActivity)

        doAsync {
            viewModel =
                SelecionarProdutoViewModel(AppDatabase.getDatabase((this@ConfirmacaoPedidoActivity)))
            listaProdutosSelecionados = viewModel.getAllProdutoVenda() as MutableList<ProdutoVenda>

            try {
                db.produtoVendaDao().delete(produtoVenda)

                totalPedido = (db.produtoVendaDao().getProdutoVendaValor()).sum()

                uiThread {
                    var totalPedidoString = totalPedido.toString()
                    if ((totalPedido.toString().length - totalPedido.toString().indexOf('.')) < 3)
                        totalPedidoString += "0"
                    textValorTotal.text = totalPedidoString.replace(".", ",")
                }

                uiThread {
                    Toast.makeText(
                        this@ConfirmacaoPedidoActivity,
                        "Item Removido!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@ConfirmacaoPedidoActivity,
                    "Nao foi possivel remover!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            listaProdutosSelecionados = viewModel.getAllProdutoVenda() as MutableList<ProdutoVenda>
            uiThread {
                carrinhoAdapter.swapData(listaProdutosSelecionados)
            }
        }
    }

}