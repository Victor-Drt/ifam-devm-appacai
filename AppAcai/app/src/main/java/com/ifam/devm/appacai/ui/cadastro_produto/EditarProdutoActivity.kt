package com.ifam.devm.appacai.ui.cadastro_produto

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.model.TipoProduto
import com.ifam.devm.appacai.repository.room.AppDatabase
import kotlinx.android.synthetic.main.activity_cadastrar_produto.*
import kotlinx.android.synthetic.main.activity_editar_produto.*
import kotlinx.android.synthetic.main.activity_editar_produto.textCliqueInserirImagemEdit
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

class EditarProdutoActivity : AppCompatActivity() {
    //    produto
    private lateinit var produto: Produto
    private lateinit var prodViewModel : CadastrarProdutoViewModel

    //    atributos
    private var nome: String = ""
    private var descricao: String = ""
    private var tipo: String = ""
    private var valor: String = ""
    val COD_IMAGE = 101
    var imageBitMap: Bitmap? = null
    var fotoFinal: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_produto)

        toolbarEditarProduto.setOnClickListener {
            onBackPressed()
            finish()
        }

        val itens: Array<TipoProduto> = TipoProduto.values()
        val adapter: ArrayAdapter<TipoProduto> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itens)

        typesFilterSpinnerEdit.setAdapter(adapter)

        typesFilterSpinnerEdit.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                tipo = typesFilterSpinnerEdit.text.toString()
            }

        textCliqueInserirImagemEdit.setOnClickListener {
            abrirGaleria()
        }

        btSalvarProduto.setOnClickListener {
            if (verificarCampos()) {
//                Banco recebe novos valores
                produto.nome = txtNomeEditProduto.text.toString()
                produto.descricao = txtDescricaoEditProduto.text.toString()
                produto.tipo = tipo
                produto.valor = txtValorEditProduto.text.toString().toFloat()

                if (fotoFinal == null) {
                    println("foto Vazia")
                } else {
                    produto.foto = fotoFinal
                }

                Toast.makeText(
                    applicationContext,
                    "Atualizado!!", Toast.LENGTH_LONG
                ).show()

//                Atualiza os dados no banco
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.produtoDao().update(produto)
                }
                finish()
            }

        }

        btExcluirProduto.setOnClickListener {
//              inicia dialog view quando clicado
            val view =
                View.inflate(this, R.layout.dialog_confirmar_exclusao, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            //mostra alert dialog para validar o sair sem salvar ou não
            val dialog = builder.create()
            dialog.show()

            view.btAlertExcluir.setOnClickListener {
                //entra em contato com o banco de dados e exclui o cliente
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.produtoDao().delete(produto)
                }
                dialog.dismiss()
                //cria uma intent enviando as informaçoes do produto para a prox tela
                val produtoJson = Gson().toJson(produto)
                val intentVisualizarFuncionario =
                    Intent(this, VisualizarProdutoActivity::class.java)
                intentVisualizarFuncionario.putExtra("produto", produtoJson)
                startActivity(intentVisualizarFuncionario)
                finish()
            }

            view.btAlertCancelarRemover.setOnClickListener {
                dialog.dismiss()
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
                try {
                    //lendo URI com a imagem
                    val inputStream = contentResolver.openInputStream((data.data!!))
                    //transformando o resultado em bitmap
                    imageBitMap = BitmapFactory.decodeStream(inputStream)
                    //exibir a imagem no aplicativo
                    imageProdutoEdit.setImageBitmap(imageBitMap)


                    var saida: ByteArrayOutputStream = ByteArrayOutputStream()
                    imageBitMap?.compress(Bitmap.CompressFormat.PNG, 100, saida)
                    fotoFinal = saida.toByteArray()
                } catch (e : Exception) {
                    if (produto?.foto != null) {
                        var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
                        imageProdutoEdit?.setImageBitmap(fotoproduto)
                    }
                }

            }
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
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@EditarProdutoActivity))
            produto = prodViewModel.consultarProdutoExistente(produtoNome.toString())

            println("Produto ${produto.nome}")

            uiThread {
                if (produto?.foto != null) {
                    var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
                    imageProdutoEdit?.setImageBitmap(fotoproduto)
                }
                txtNomeEditProduto.setText(produto.nome)
                txtDescricaoEditProduto.setText(produto.descricao)
                txtValorEditProduto.setText((produto.valor).toString())
                tipo = (produto.tipo)
            }
        }
    }

    //    Verifica se o formulario foi preenchido corretamente
    private fun verificarCampos(): Boolean {
        setCamposNull()

        nome = txtNomeEditProduto.text.toString()
        descricao = txtDescricaoEditProduto.text.toString()
        valor = txtValorEditProduto.text.toString()

        if (nome.isEmpty()) {
            editProdutoLayoutInputTextName.error = "Insira o nome do produto!"
            return false
        } else if (descricao.isEmpty()) {
            editProdutoLayoutInputTextDescricao.error = "Insira uma descricao para o produto!"
            return false
        } else if (valor.isEmpty()) {
            editProdutoLayoutInputTextValor.error = "Insira um valor para o produto!"
            return false
        } else if (tipo.isEmpty()) {
            typesFilterContainerEdit.error = "Insira um tipo para o produto!"
            return false
        } else {
            return true
        }
    }

    private fun setCamposNull() {
        editProdutoLayoutInputTextName.error = null
        editProdutoLayoutInputTextDescricao.error = null
        editProdutoLayoutInputTextValor.error = null
        typesFilterContainerEdit.error = null
    }
}