package com.ifam.devm.appacai.ui.funcionarios

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.home.HomeActivity
import com.ifam.devm.appacai.ui.home.VendedoresFragment
import com.ifam.devm.appacai.utils.CPFUtil
import com.ifam.devm.appacai.utils.Mask
import com.ifam.devm.appacai.utils.MaskCopy.MaskChangedListener
import com.ifam.devm.appacai.utils.MaskCopy.MaskSL
import com.ifam.devm.appacai.utils.MaskCopy.MaskStyle
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_editar_funcionario.*
import kotlinx.android.synthetic.main.activity_editar_funcionario.cadInputEmail
import kotlinx.android.synthetic.main.activity_editar_funcionario.cadInputNome
import kotlinx.android.synthetic.main.activity_editar_funcionario.cadInputTelefone
import kotlinx.android.synthetic.main.activity_editar_produto.*
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.*
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

class EditarFuncionarioActivity : AppCompatActivity() {
    //funcionario
    private lateinit var funcionario: Funcionario
    private lateinit var funcViewModel: CadastrarFuncionarioViewModel

    //Atributos
    private var nome: String = ""
    private var email: String = ""
    private var telefone: String = ""
    private var cpf: String = ""
    private var metaVenda: Double = 0.0
    private var totalVenda: Double = 0.0
    val COD_IMAGE = 101
    var imageBitMap: Bitmap? = null
    var fotoFinal: ByteArray? = null

    //definindo padrão da mask para o telefone
    private val PADRAO_TELEFONE = "+55 (__) 9____-____"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_funcionario)

        toolbarEditarFuncionario.setOnClickListener {
            onBackPressed()
            finish()
        }

        textClickImgFuncEdit.setOnClickListener {
            abrirGaleria()
        }

//        Masks
        textInputCPF.addTextChangedListener(
            Mask.mask(
                "###.###.###-##",
                textInputCPF))

        //mask para configurar a exibição do campo de Telefone quando digitado
        val maskPhone = MaskSL(
            value = PADRAO_TELEFONE,
            character = '_',
            style = MaskStyle.NORMAL
        )
        var listener = MaskChangedListener(maskPhone)
        //textField.addTextChangedListener(listener)
        textInputTelefone.addTextChangedListener(listener)

        btSalvarFuncionario.setOnClickListener {
            if (verificarCampos()) {
                funcionario.nome_funcionario = textInputNome.text.toString()
                funcionario.email_funcionario = textInputEmail.text.toString()
                funcionario.cpf_funcionario = textInputCPF.text.toString()
                funcionario.telefone_funcionario = textInputTelefone.text.toString()
                funcionario.meta_vendas = textInputMeta.text.toString().toDouble()

                if (fotoFinal == null) {
                    println("foto Vazia")
                } else {
                    funcionario.foto = fotoFinal
                }


                Toast.makeText(applicationContext, "Atualizado", Toast.LENGTH_LONG).show()
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.funcionarioDao().update(funcionario)
                }
                finish()
            }
        }

        btExcluirFuncionario.setOnClickListener {
            val view = View.inflate(this, R.layout.dialog_confirmar_exclusao, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            view.btAlertExcluir.setOnClickListener {
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.funcionarioDao().delete(funcionario)
                }

                dialog.dismiss()

                val funcionarioJson = Gson().toJson(funcionario)
                val intentVisualizarFun = Intent(this, HomeActivity::class.java)
                intentVisualizarFun.putExtra("funcionario", funcionarioJson)
                startActivity(intentVisualizarFun)
                finish()
            }

            view.btAlertCancelarRemover.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun onStart() {
        carregaDadosDoBanco()
        super.onStart()
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
                    imageFuncionarioEdit.setImageBitmap(imageBitMap)

                    //alterando pra salvar no banco
                    var saida: ByteArrayOutputStream = ByteArrayOutputStream()
                    imageBitMap?.compress(Bitmap.CompressFormat.PNG, 100, saida)
                    fotoFinal = saida.toByteArray()
                } catch (e : Exception) {
                    if (funcionario?.foto != null) {
                        var fotofuncionario = BitmapFactory.decodeByteArray(funcionario.foto, 0, (funcionario.foto)?.size!!)
                        imageFuncionarioEdit?.setImageBitmap(fotofuncionario)
                    }
                }
            }
        }
    }

    private fun carregaDadosDoBanco() {
        val intent = intent
        val funcionarioNome = intent.getStringExtra("funcionario_nome")

        doAsync {
            funcViewModel =
                CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@EditarFuncionarioActivity))
            funcionario = funcViewModel.consutaFuncionarioPorNome(funcionarioNome.toString())

            println("Funcionario ${funcionario.nome_funcionario}")

            uiThread {
                if (funcionario?.foto != null) {
                    var fotofuncionario = BitmapFactory.decodeByteArray(funcionario.foto, 0, (funcionario.foto)?.size!!)
                    imageFuncionarioEdit?.setImageBitmap(fotofuncionario)
                }
                textInputNome.setText(funcionario.nome_funcionario)
                textInputEmail.setText(funcionario.email_funcionario)
                textInputTelefone.setText(funcionario.telefone_funcionario)
                textInputCPF.setText(funcionario.cpf_funcionario)
                textInputMeta.setText(funcionario.meta_vendas.toString())
            }
        }


    }

    fun verificarCampos(): Boolean {
        setCamposNull()

        nome = textInputNome.text.toString()
        email = textInputEmail.text.toString()
        telefone = textInputTelefone.text.toString()
        cpf = textInputCPF.text.toString()
        metaVenda = textInputMeta.text.toString().toDouble()

        if (nome.isEmpty()) {
            cadInputNome.error = "Por-favor insira um nome"
            return false
        } else if (email.isEmpty()) {
            cadInputEmail.error = "Por-favor informe um email"
            return false
        } else if (telefone.isEmpty()) {
            cadInputTelefone.error = "Por-favor informe seu número de telefone"
            return false
        } else if (cpf.isEmpty()) {
            cadInputCPF.error = "Por-favor informe o seu CPF"
            return false
        }  else if (!CPFUtil.validarCadastroCPF(cpf)){
            cadInputCPF.error = "CPF inválido!"
            Log.d("ERROR", "CPF inválido")
            return false
        } else if (metaVenda == 0.0) {
            cadInputMetaVendas.error = "Por-favor insira sua meta de vendas"
            return false
        } else {
            return true
        }


    }

    fun setCamposNull() {
        cadInputNome.error = null
        cadInputEmail.error = null
        cadInputTelefone.error = null
        cadInputCPF.error = null
        cadInputMeta.error = null
    }
}