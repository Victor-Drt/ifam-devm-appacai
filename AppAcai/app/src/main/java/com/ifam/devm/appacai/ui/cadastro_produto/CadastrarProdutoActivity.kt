package com.ifam.devm.appacai.ui.cadastro_produto

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64.encode
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.TipoProduto
import com.ifam.devm.appacai.repository.room.AppDatabase
import kotlinx.android.synthetic.main.activity_cadastrar_produto.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.URLEncoder.encode
import java.sql.Blob
import java.util.*

class CadastrarProdutoActivity : AppCompatActivity() {
    //    produto
    private lateinit var produto: Produto

    //    atributos
    private var id: Long = 0L
    private var nome: String = ""
    private var descricao: String = ""
    private var tipo: String = ""
    private var valor: String = ""
    val COD_IMAGE = 101
    var imageBitMap: Bitmap? = null
    var fotoFinal: ByteArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_produto)

//        spinner
        val itens: Array<TipoProduto> = TipoProduto.values()
        val adapter: ArrayAdapter<TipoProduto> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itens)
        typesFilterSpinner.setAdapter(adapter)

        typesFilterSpinner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                tipo = typesFilterSpinner.text.toString()
            }


//        Instanciando botão de retorno da ToolBar
        toolbarCadastrarProduto.setNavigationOnClickListener {
            onBackPressed()
        }

        textCliqueInserirImagem.setOnClickListener {
            abrirGaleria()
        }

//        setando os erros para null
        setCamposNull()

        produto = Produto()

//        setando botao cadastrar
        btCadastrarProduto.setOnClickListener {
            id = gerarIdCadastro()
            nome = txtNomeCadProduto.text.toString()
            descricao = txtDescricaoCadProduto.text.toString()
            valor = txtValorCadProduto.text.toString()

            if (verificarCampos()) {
                doAsync {
                    val produtoViewModel =
                        CadastrarProdutoViewModel(AppDatabase.getDatabase(this@CadastrarProdutoActivity))

                    val resultadoConsultaProduto: Produto =
                        produtoViewModel.consultarProdutoExistente(nome)

                    try {
                        println("Nome - ${resultadoConsultaProduto.nome}")
                        uiThread {
//                        se o nome do produto ja estiver cadastrado, uma mensagem sera emitida
                            if (resultadoConsultaProduto.descricao.uppercase() == descricao.uppercase()) {
                                println("Descricao - ${resultadoConsultaProduto.descricao} e $descricao")
                                cadProdutoLayoutInputTextDescricao.error = "Descricao nao pode ser Igual!"
                            } else {
                                try {
                                    uiThread {
                                        cadastrarProduto(
                                            id,
                                            nome,
                                            descricao,
                                            tipo,
                                            valor.toFloat(),
                                            0.0f,
                                            0,
                                            fotoFinal as ByteArray,
                                            0
                                        )
                                        Toast.makeText(
                                            this@CadastrarProdutoActivity,
                                            "Produto cadastrado com sucesso!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onBackPressed()
                                        finish()
                                    }
                                } catch (e: Exception) {
//                            erro no envio das informacoes
                                    uiThread {
                                        Toast.makeText(
                                            this@CadastrarProdutoActivity,
                                            "Erro no envio do formulario!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        try {

                            uiThread {
                                cadastrarProduto(
                                    id,
                                    nome,
                                    descricao,
                                    tipo,
                                    valor.toFloat(),
                                    0.0f,
                                    0,
                                    fotoFinal as ByteArray,
                                    0
                                )
                                Toast.makeText(
                                    this@CadastrarProdutoActivity,
                                    "Produto cadastrado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressed()
                                finish()
                            }
                        } catch (e: Exception) {
//                            erro no envio das informacoes
                            uiThread {
                                Toast.makeText(
                                    this@CadastrarProdutoActivity,
                                    "Erro no envio do formulario!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }
            }
        }

    }

    private fun abrirGaleria() {
        //definindo uma intent para acao de conteudo
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        //definindo filtro para imagens
        intent.type = "image/*"

        //inicializando a activity com o resultado
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), COD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == COD_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //lendo URI com a imagem
                val inputStream = contentResolver.openInputStream((data.data!!))
                //transformando o resultado em bitmap
                imageBitMap = BitmapFactory.decodeStream(inputStream)
                //exibir a imagem no aplicativo
                imageProdutoCad.setImageBitmap(imageBitMap)

                var saida: ByteArrayOutputStream = ByteArrayOutputStream()
                imageBitMap?.compress(Bitmap.CompressFormat.PNG, 100, saida)
                fotoFinal = saida.toByteArray()
            }
        }
    }

    private fun cadastrarProduto(
        id: Long,
        nome: String,
        descricao: String,
        tipo: String,
        valor: Float,
        aval: Float,
        votos: Int,
        foto: ByteArray,
        freq: Int
    ) {
        produto = Produto(id, nome, descricao, tipo, valor, aval, votos, foto, freq)

        if (foto != null) {
            println("Foto nao ta nula ><")
        }

        val db = AppDatabase.getDatabase(this)

        doAsync {
            db.produtoDao().insert(produto)
        }
    }

    //    Verifica se o formulario foi preenchido corretamente
    private fun verificarCampos(): Boolean {
        setCamposNull()
        if (nome.isEmpty()) {
            cadProdutoLayoutInputTextName.error = "Insira o nome do produto!"
            return false
        } else if (descricao.isEmpty()) {
            cadProdutoLayoutInputTextDescricao.error = "Insira uma descricao para o produto!"
            return false
        } else if (valor.isEmpty()) {
            cadProdutoLayoutInputTextValor.error = "Insira um valor para o produto!"
            return false
        } else if (tipo == null) {
            typesFilterSpinner.error = "Insira um tipo para o produto!"
            return false
        } else if (imageBitMap == null) {
            textCliqueInserirImagem.error = "Insira uma imagem!"
            return false
        } else {
            return true
        }
    }

    private fun gerarIdCadastro(): Long {
        val leftLimit = 1L
        val rightLimit = 1000000000L
        val generatedLong = leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()
        return generatedLong
    }

    private fun setCamposNull() {
        cadProdutoLayoutInputTextName.error = null
        cadProdutoLayoutInputTextDescricao.error = null
        cadProdutoLayoutInputTextValor.error = null
    }
}