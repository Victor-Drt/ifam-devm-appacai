package com.ifam.devm.appacai.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.FuncionariosAdapter
import com.ifam.devm.appacai.adapters.ProdutosAdapter
import com.ifam.devm.appacai.model.*
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoViewModel
import com.ifam.devm.appacai.ui.cadastro_produto.EditarProdutoActivity
import com.ifam.devm.appacai.ui.cadastro_produto.VisualizarProdutoActivity
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.funcionarios.VisualizarFuncionarioActivity
import com.ifam.devm.appacai.ui.startup.SplashActivity
import kotlinx.android.synthetic.main.fragment_produtos.*
import kotlinx.android.synthetic.main.fragment_vendas.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class VendasFragment : Fragment() {

    private lateinit var produtosCadastrados: MutableList<Produto>
    private lateinit var produtosAdapter: ProdutosAdapter

    private val CREATE_FILE = 1

    // Request code for selecting a PDF document.
    private val PICK_PDF_FILE = 2

    private lateinit var funcionariosCadastrados: MutableList<Funcionario>
    private lateinit var listaFunc: MutableList<Funcionario>
    private lateinit var funcionariosAdapter: FuncionariosAdapter

    private lateinit var relatorioVenda: RelatorioVenda
    private lateinit var usuario: Usuario

    private lateinit var funcionario: Funcionario
    private lateinit var pagamento: Pagamento
    private lateinit var listaPagamentos: MutableList<Pagamento>
    private lateinit var listaPagamentoHj: MutableList<Pagamento>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendas, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formataData = SimpleDateFormat("dd-MM-yyyy")
        val data = Date()
        val dataFormatadaHj = formataData.format(data)


//        gerar relatorio
        toolbarVendas.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.imprimir_relatorio -> {
                    try {
                        var mediaAval = (produtosCadastrados.map { it.avaliacao }
                            .sum()) / produtosCadastrados.size
                        var funcSemana =
                            funcionariosCadastrados.sortedWith(compareByDescending({ it.total_vendas }))
                                .first()
                        var maisVendido =
                            produtosCadastrados.sortedWith(compareByDescending({ it.freqVenda }))
                                .first()

                        relatorioVenda = RelatorioVenda(
                            gerarIdCadastro(),
                            usuario.nomeEmpresa,
                            dataFormatadaHj,
                            textTotalHj.text.toString().replace(",", ".").toDouble(),
                            textValorTotalVendas.text.toString().replace(",", ".").toDouble(),
                            mediaAval,
                            funcSemana.nome_funcionario,
                            maisVendido.nome
                        )
                        gerarRelatorioPDF(relatorioVenda)
                    } catch (e: Exception) {
                        Log.d("Erro", "Listas Vazias")
                    }
                    true
                }
                R.id.zerar_contagem_caixa -> {
                    confirmarReset()
                }
            }
            true
        }


// inicializa RecyclerView Produtos
        produtosAdapter = ProdutosAdapter(
            this@VendasFragment.requireContext(),
            mutableListOf(),
            ::onClickItem,
            ::editarItemClick,
            ::deleteItemClick
        )

        rvProdutosVendasFragment.adapter = produtosAdapter
        rvProdutosVendasFragment.layoutManager =
            LinearLayoutManager(this@VendasFragment.requireContext())

//        Inicializa viewModel e obtém lista inicial de produtos
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val produtosViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@VendasFragment.requireContext()))

            produtosCadastrados = produtosViewModel.getAllProduto() as MutableList<Produto>
            Log.d("M", produtosCadastrados.toString())

            uiThread {
                produtosAdapter.swapData(produtosCadastrados.sortedWith(compareByDescending({ it.freqVenda })))
            }
        }

        // inicializa RecyclerView
        funcionariosAdapter = FuncionariosAdapter(
            this@VendasFragment.requireContext(),
            mutableListOf(),
            ::onClickItemVendedor,
            ::editarItemVendedorClick,
            ::deleteItemVendedorClick,
        )

        rvVendedoresVendasFragment.adapter = funcionariosAdapter
        rvVendedoresVendasFragment.layoutManager =
            LinearLayoutManager(this@VendasFragment.requireContext())

//        Inicializa viewModel e obtém lista inicial de produtos
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val funcionariosViewModel =
                CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VendasFragment.requireContext()))

            funcionariosCadastrados =
                funcionariosViewModel.getAllFuncionarios() as MutableList<Funcionario>
            Log.d("M", funcionariosCadastrados.toString())

            uiThread {
                funcionariosAdapter.swapData(
                    funcionariosCadastrados.sortedWith(
                        compareByDescending(
                            { it.total_vendas })
                    )
                )
            }
        }
    }

    private fun confirmarReset() {
        var cond = false
        val alertDialog: AlertDialog? = this.context.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.text_dialog_reset)
                setMessage(R.string.text_message_reset_values)
                setPositiveButton(R.string.text_dialog_positive,
                    DialogInterface.OnClickListener { dialog, id ->
                        val db = AppDatabase.getDatabase(this@VendasFragment.requireContext())
                        doAsync {
                            db.pagamentoDao().deleteAll()
                            for (func in listaFunc) {
                                funcionario = db.funcionarioDao().getFuncionarioByID(func.id_funcionario)
                                funcionario.total_vendas = 0.00
                                db.funcionarioDao().update(funcionario)
                            }
                            Toast.makeText(
                                this@VendasFragment.requireContext(),
                                "Valores Redefinidos!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                setNegativeButton(R.string.text_dialog_cancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        dialog.cancel()
                    })
            }
            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun gerarRelatorioPDF(relatorioVenda: RelatorioVenda) {
        try {
            //        cria um documento para gerar o PDF
            val documentoPDF = PdfDocument()

//        especifica detalhes da pagina
            var detalhesDaPagina: PdfDocument.PageInfo =
                PdfDocument.PageInfo.Builder(500, 600, 1).create()

//        cria primeira pagina
            val novaPagina: PdfDocument.Page = documentoPDF.startPage(detalhesDaPagina)

            val canvas: Canvas = novaPagina.canvas

            val corDoTexto: Paint = Paint()
            corDoTexto.color = Color.BLACK

            canvas.drawText(
                "DATA DO RELATORIO - ${relatorioVenda.dataRelatorio} - LAMV ACAI",
                105F,
                100F,
                corDoTexto
            )
            canvas.drawText("NOME DA LOJA - ${relatorioVenda.nomeLoja}", 105F, 150F, corDoTexto)
            canvas.drawText(
                "VALOR ARRECADADO NO DIA - R$${
                    relatorioVenda.valorDia.toString().replace(".", ",")
                }",
                105F,
                200F,
                corDoTexto
            )
            canvas.drawText(
                "VALOR TOTAL - R$${relatorioVenda.valorSemana.toString().replace(".", ",")}",
                105F,
                250F,
                corDoTexto
            )
            canvas.drawText(
                "PRODUTO MAIS VENDIDO - ${relatorioVenda.maisVendidos}",
                105F,
                300F,
                corDoTexto
            )
            canvas.drawText(
                "FUNCIONARIO QUE MAIS VENDEU - ${relatorioVenda.funcSemana}",
                105F,
                350F,
                corDoTexto
            )
            canvas.drawText(
                "MEDIA DE AVALIACAO DOS PRODUTOS - ${
                    relatorioVenda.mediaAvalProd.toString().replace(".", ",")
                }",
                105F,
                400F,
                corDoTexto
            )

//        finaliza primeira pagina
            documentoPDF.finishPage(novaPagina)

            var targetPdf: String = "/Download/"
            var filePath: File = File(
                Environment.getExternalStorageDirectory().path + targetPdf,
                "relatorio_lamv.pdf"
            )

            try {
                documentoPDF.writeTo(FileOutputStream(filePath))
                Toast.makeText(
                    this@VendasFragment.requireContext(),
                    "Relatorio Gerado com Sucesso... no diretorio ${filePath}",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Log.d("Erro na geracao do Relatorio", "FALHA AO GERAR PDF - ${e}")
                Toast.makeText(
                    this@VendasFragment.requireContext(),
                    "Não foi possivel gerar o relatorio!",
                    Toast.LENGTH_SHORT
                ).show()
            }
//        fecha arquivo criado
            documentoPDF.close()
        } catch (e: Exception) {
            Log.d("Erro na geracao do Relatorio", "FALHA AO GERAR PDF ANTES DE TENTAR  - ${e}")
        }
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.getDatabase(this@VendasFragment.requireContext())

        doAsync {

            val formataData = SimpleDateFormat("dd-MM-yyyy")
            val data = Date()
            val dataFormatadaHj = formataData.format(data)

            usuario = db.usuarioDao().getUsuario()
            listaFunc = db.funcionarioDao().getAllFuncionario() as MutableList<Funcionario>
            listaPagamentos = db.pagamentoDao().getAllPagamentos() as MutableList<Pagamento>

            try {
                var valorTotalFunc = listaFunc.map { it.total_vendas }

                for (i in listaPagamentos.map { it.dataPagamento }) {
                    if (i == dataFormatadaHj) {
                        doAsync {
                            listaPagamentoHj =
                                db.pagamentoDao().getPagamentoByDia(i) as MutableList<Pagamento>

//                            println("${listaPagamentoHj}")
                            uiThread {
                                var tvValor = valorTotalFunc.sum().toString()
                                tvValor = tvValor.replace('.', ',')
                                if (tvValor.length - tvValor.indexOf(',') < 3)
                                    tvValor += '1'.toString()
                                textValorTotalVendas.text = tvValor
                                println(" Valor Total ${tvValor} ${dataFormatadaHj}")

                                var tvValorHj = listaPagamentoHj.map { it.valor }.sum().toString()
                                tvValorHj = tvValorHj.replace('.', ',')
                                if (tvValorHj.length - tvValorHj.indexOf(',') < 3)
                                    tvValorHj += '0'
                                textTotalHj.text = tvValorHj

                                println(" Valor Total ${tvValorHj} ${dataFormatadaHj}")
                            }
                        }
                    } else {
                        doAsync {
                            listaPagamentoHj =
                                db.pagamentoDao().getPagamentoByDia(i) as MutableList<Pagamento>

//                            println("${listaPagamentoHj}")
                            uiThread {
                                var tvValor = valorTotalFunc.sum().toString()
                                tvValor = tvValor.replace('.', ',')
                                if (tvValor.length - tvValor.indexOf(',') < 3)
                                    tvValor += '1'.toString()
                                textValorTotalVendas.text = tvValor
                                println(" Valor Total ${tvValor} ${dataFormatadaHj}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                uiThread {
                    textValorTotalVendas.text = "0,00"
                    textTotalHj.text = "0,00"
                }

            }
        }

    }

    //    Funcionarios
    private fun onClickItemVendedor(funcionario: Funcionario) {
        val intent =
            Intent(this@VendasFragment.requireContext(), VisualizarFuncionarioActivity::class.java)
        intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
        startActivity(intent)
    }

    private fun deleteItemVendedorClick(funcionario: Funcionario) {
        val data = AppDatabase.getDatabase(this@VendasFragment.requireContext())
        doAsync {
            data.funcionarioDao().delete(funcionario)
        }
    }

    private fun editarItemVendedorClick(funcionario: Funcionario) {
        val intent =
            Intent(this@VendasFragment.requireContext(), EditarFuncionarioActivity::class.java)
        intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
        startActivity(intent)
    }

    //    Produtos
    private fun onClickItem(produto: Produto) {
        val intent =
            Intent(this@VendasFragment.requireContext(), VisualizarProdutoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        startActivity(intent)
    }

    private fun deleteItemClick(produto: Produto) {
        //entra em contato com o banco de dados e exclui o produto
        val data = AppDatabase.getDatabase(this@VendasFragment.requireContext())
        doAsync {
            data.produtoDao().delete(produto)
        }
    }

    private fun editarItemClick(produto: Produto) {
        //cria uma intent enviando as informaçoes do produto para a prox tela
        val intent = Intent(this@VendasFragment.requireContext(), EditarProdutoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        startActivity(intent)
    }

    private fun gerarIdCadastro(): Long {
        val leftLimit = 1L
        val rightLimit = 1000000000L
        val generatedLong = leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()
        return generatedLong
    }
}
