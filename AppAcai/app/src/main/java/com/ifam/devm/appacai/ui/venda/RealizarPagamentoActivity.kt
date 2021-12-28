package com.ifam.devm.appacai.ui.venda

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.ProdutoVenda
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import kotlinx.android.synthetic.main.activity_realizar_pagamento.*
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.DecimalFormat

class RealizarPagamentoActivity : AppCompatActivity() {
    private var totalPedido = 0.00f
    private var nomeFuncionario = ""
    private lateinit var funcionario: Funcionario
    private lateinit var produto: Produto
    private lateinit var listaItemVenda: MutableList<ProdutoVenda>
    private lateinit var usuario: Usuario
    private var df = DecimalFormat("0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realizar_pagamento)

//        botao de finalizar venda
        btnFinalizarVenda.setOnClickListener {
//            atribui valor da compra ao valor arrecado do vendedor
            val db = AppDatabase.getDatabase(this)
            doAsync {
                listaItemVenda =
                    db.produtoVendaDao().getAllProdutosVenda() as MutableList<ProdutoVenda>
                funcionario = db.funcionarioDao().getFuncionarioByNome(nomeFuncionario)
                funcionario.total_vendas += df.format(totalPedido).replace(",", ".").toDouble()
                db.funcionarioDao().update(funcionario)

                try {
                    var listaIds = arrayListOf<Long>()
                    for (item in listaItemVenda) {
                        println("ID - ${item.id_venda}")
                        listaIds.add(item.id_venda)
                    }
                    doAsync {
                        for (i in listaIds.indices) {
                            produto = db.produtoDao().produtoById(listaIds[i])
                            produto.freqVenda += 1

                            db.produtoDao().update(produto)
                            println("Frequencia salva ${produto.nome} - ${produto.freqVenda}")
                        }
                    }
                } catch (e: Exception) {
                    println("NAO DEU PRA PEGAR A LISTA")
                }

                uiThread {
                    println(funcionario)
                    println(funcionario.total_vendas)
                }

                db.produtoVendaDao().deleteAllProdVenda()
                println("Carrinho Limpo")
            }
            val intent = Intent(this, VendaRealizadaActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            intent.putExtra("valorCompraHJ", totalPedido)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        nomeFuncionario = intent.getStringExtra("funcionario_nome").toString()
        println(nomeFuncionario)

        val db = AppDatabase.getDatabase(this)
        doAsync {
            usuario = db.usuarioDao().getUsuario()
            funcionario = db.funcionarioDao().getFuncionarioByNome(nomeFuncionario)

            uiThread {
                if (usuario?.foto != null) {
                    var fotoQr =
                        BitmapFactory.decodeByteArray(usuario.foto, 0, (usuario.foto)?.size!!)
                    imageQRCodeConfirm?.setImageBitmap(fotoQr)
                }

                text_pixLoja.text = usuario.chavePix
            }
        }

//        Imprime o valor da compra
        totalPedido = intent.getFloatExtra("totalPedido", 0.00f)
        var totalPedidoString = totalPedido.toString()
        if ((totalPedido.toString().length - totalPedido.toString().indexOf('.')) < 3)
            totalPedidoString += "0"
        textValorCompra.text = totalPedidoString.replace(".", ",")
    }
}