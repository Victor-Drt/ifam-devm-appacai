package com.ifam.devm.appacai.ui.venda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.SelecProdutosAdapter
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.ProdutoVenda
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoViewModel
import com.ifam.devm.appacai.ui.home.VendedorMainActivity
import kotlinx.android.synthetic.main.activity_selecao_produtos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.security.Principal
import kotlin.math.log

class SelecaoProdutosActivity : AppCompatActivity() {
    private lateinit var produtosCadastrados: MutableList<Produto>
    private lateinit var listaProdutosSelecionados: MutableList<ProdutoVenda>
    private lateinit var produtosSelecAdapter: SelecProdutosAdapter
    private lateinit var produtoVenda: ProdutoVenda
    private lateinit var nomeFuncionario: String
    private lateinit var viewModel: SelecionarProdutoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_produtos)

        toolbarSelecaoProduto.setNavigationOnClickListener {
            doAsync {
                viewModel =
                    SelecionarProdutoViewModel(AppDatabase.getDatabase((this@SelecaoProdutosActivity)))
                viewModel.deleteAllProdVenda()

                listaProdutosSelecionados = viewModel.getAllProdutoVenda() as MutableList<ProdutoVenda>

                uiThread {
                    Toast.makeText(
                        this@SelecaoProdutosActivity,
                        "Carrinho esvaziado!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            finish()
        }

        fbAbrirCarrinho.setOnClickListener {
            doAsync {
                viewModel =
                    SelecionarProdutoViewModel(AppDatabase.getDatabase((this@SelecaoProdutosActivity)))

                listaProdutosSelecionados = viewModel.getAllProdutoVenda() as MutableList<ProdutoVenda>

                try {
                    println(listaProdutosSelecionados.size)
                    uiThread {
                        if (listaProdutosSelecionados.size == 0) {
                            Toast.makeText(
                                this@SelecaoProdutosActivity,
                                "Adicione pelo menos um item ao carrinho!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val intent = Intent(this@SelecaoProdutosActivity, ConfirmacaoPedidoActivity::class.java)
                            intent.putExtra("funcionario_nome", nomeFuncionario)
                            println(nomeFuncionario)
                            startActivity(intent)
                        }
                    }
                } catch (e : Exception) {
                    Toast.makeText(
                        this@SelecaoProdutosActivity,
                        "Nao foi adicionado nenhum item!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

        produtosSelecAdapter = SelecProdutosAdapter(
            this,
            mutableListOf(),
            ::clicaBtAdd
        )

        rvListaProdutosSelec.adapter = produtosSelecAdapter
        rvListaProdutosSelec.layoutManager = LinearLayoutManager(this)

        principalText.setOnClickListener {
            produtosSelecAdapter.swapData(produtosCadastrados.filter { it.tipo == "PRINCIPAL" })
        }

        caldasText.setOnClickListener {
            produtosSelecAdapter.swapData(produtosCadastrados.filter { it.tipo == "CALDAS" })
        }

        frutasText.setOnClickListener {
            produtosSelecAdapter.swapData(produtosCadastrados.filter { it.tipo == "FRUTAS" })
        }

        complementosText.setOnClickListener {
            produtosSelecAdapter.swapData(produtosCadastrados.filter { it.tipo == "COMPLEMENTOS" })
        }


        doAsync {
            val produtosViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@SelecaoProdutosActivity))

            produtosCadastrados = produtosViewModel.getAllProduto() as MutableList<Produto>
            Log.d("M", produtosCadastrados.toString())

            uiThread {
                produtosSelecAdapter.swapData(produtosCadastrados)
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
        }

        doAsync {
            viewModel =
                SelecionarProdutoViewModel(AppDatabase.getDatabase((this@SelecaoProdutosActivity)))
            viewModel.deleteAllProdVenda()

            listaProdutosSelecionados = viewModel.getAllProdutoVenda() as MutableList<ProdutoVenda>

            uiThread {
                Toast.makeText(
                    this@SelecaoProdutosActivity,
                    "Carrinho esvaziado!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clicaBtAdd(produto: Produto) {
        doAsync {
            try {
                uiThread {
                    adicionarProdutoSelecionado(
                        gerarIdCadastro(),
                        produto.id,
                        produto.nome,
                        produto.descricao,
                        produto.valor,
                        produto.foto
                    )
                }
            } catch (e: Exception) {
//                erro no envio das informacoes
                uiThread {
                    Toast.makeText(
                        this@SelecaoProdutosActivity,
                        "Erro no envio do formulario!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun adicionarProdutoSelecionado(
        id: Long,
        idVenda: Long,
        nome: String,
        descricao: String,
        valor: Float,
        foto: ByteArray?
    ) {
        produtoVenda = ProdutoVenda(id, idVenda, nome, descricao, valor, foto)
        val db = AppDatabase.getDatabase(this)

        doAsync {
            db.produtoVendaDao().insert(produtoVenda)
        }

        Toast.makeText(
            this,
            "Produto adicionado ao carrinho!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun gerarIdCadastro(): Long {
        val leftLimit = 1L
        val rightLimit = 1000000000L
        val generatedLong = leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()
        return generatedLong
    }

    private fun shouldAllowBack(): Boolean {
        return false
    }
}