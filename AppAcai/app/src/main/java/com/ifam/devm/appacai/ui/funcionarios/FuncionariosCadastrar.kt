package com.ifam.devm.appacai.ui.funcionarios

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.home.HomeActivity
import com.ifam.devm.appacai.utils.CPFUtil
import com.ifam.devm.appacai.utils.Mask
import com.ifam.devm.appacai.utils.MaskCopy.MaskChangedListener
import com.ifam.devm.appacai.utils.MaskCopy.MaskSL
import com.ifam.devm.appacai.utils.MaskCopy.MaskStyle
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_cadastrar_produto.*
import kotlinx.android.synthetic.main.fragment_vendedores.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.lang.Exception

class FuncionariosCadastrar : AppCompatActivity() {
    private lateinit var funcionario: Funcionario

    private var id: Long = 0L
    private lateinit var nome: String;
    private lateinit var email: String;
    private lateinit var telefone: String;
    private lateinit var cpf: String;
    private lateinit var totalVendas: String;
    private lateinit var metaVendas: String;
    private lateinit var senha: String
    val COD_IMAGE = 101
    var imageBitMap: Bitmap? = null
    var fotoFinal: ByteArray? = null

    //definindo padrão da mask para o telefone
    private val PADRAO_TELEFONE = "+55 (__) 9____-____"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_cadastra_funcionario)

        setNullFields()

        toolbarCadastrarFuncionario.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        textClickImgFunc.setOnClickListener {
            abrirGaleria()
        }

        //masks
        textCpfInput.addTextChangedListener(
            Mask.mask(
                "###.###.###-##",
                textCpfInput))

        //mask para configurar a exibição do campo de Telefone quando digitado
        val maskPhone = MaskSL(
            value = PADRAO_TELEFONE,
            character = '_',
            style = MaskStyle.NORMAL
        )
        var listener = MaskChangedListener(maskPhone)
        //textField.addTextChangedListener(listener)
        textTelefoneInput.addTextChangedListener(listener)

        btn_cadastrar.setOnClickListener {
            id = gerarIdCadastro()
            nome = textNomeInput.text.toString()
            email = textEmailInput.text.toString()
            telefone = textTelefoneInput.text.toString()
            cpf = textCpfInput.text.toString()
            metaVendas = textMetaVendasInput.text.toString()
            totalVendas = "0.00"
            senha = textInputSenha.text.toString()

            if (validarDados()) {
                doAsync {
                    val funcionarioViewModel =
                        CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@FuncionariosCadastrar))

                    val resultadoConsultaFunc =
                        funcionarioViewModel.consutaFuncionarioPorCPF(cpf)

                    try {
                        println("CPF - ${resultadoConsultaFunc.cpf_funcionario}")
                        if (resultadoConsultaFunc.cpf_funcionario == cpf) {
                            uiThread {
                                cadInputCpf.error = "CPF ja cadastrado!"
                            }
                        }
                    } catch (e: Exception) {
                        try {
                            //aqui dentro
                            uiThread {
                                cadastrarFuncionario(
                                    id,
                                    nome,
                                    email,
                                    telefone,
                                    cpf,
                                    fotoFinal!!,
                                    metaVendas.toDouble(),
                                    totalVendas.toDouble(),
                                    senha
                                )
                                Toast.makeText(
                                    this@FuncionariosCadastrar,
                                    "Cadastro Realizado",
                                    Toast.LENGTH_LONG
                                ).show()
                                onBackPressed()
                                finish()
                            }
                        } catch (e: Exception) {
                            //  erro no envio das informacoes
                            uiThread {
                                Toast.makeText(
                                    this@FuncionariosCadastrar,
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

    private fun cadastrarFuncionario(
        id: Long,
        nome: String,
        email: String,
        telefone: String,
        cpf: String,
        foto: ByteArray,
        meta: Double,
        venda: Double,
        senha: String
    ) {
        funcionario = Funcionario(id, nome, email, telefone, cpf, foto, meta, venda, senha)

        println("Foto nao ta nula ><")

        val db = AppDatabase.getDatabase(this)
        doAsync {
            db.funcionarioDao().insert(funcionario)

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
                imageFuncionarioCad.setImageBitmap(imageBitMap)


                var saida: ByteArrayOutputStream = ByteArrayOutputStream()
                imageBitMap?.compress(Bitmap.CompressFormat.PNG, 100, saida)
                fotoFinal = saida.toByteArray()
            }
        }
    }

    private fun gerarIdCadastro(): Long {
        val leftLimit = 1L
        val rightLimit = 1000000000L
        val generatedLong = leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()
        return generatedLong
    }

    private fun validarDados(): Boolean {
        nome = textNomeInput.text.toString()
        email = textEmailInput.text.toString()
        telefone = textTelefoneInput.text.toString()
        cpf = textCpfInput.text.toString()
        metaVendas = textMetaVendasInput.text.toString()
        senha = textInputSenha.text.toString()

        if (nome.isEmpty()) {
            cadInputNome.error = "Insira um nome!"
            return false
        }
        cadInputNome.error = null

        if (email.isEmpty()) {
            cadInputEmail.error = "Insira um e-mail!"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            cadInputEmail.error = "E-mail inválido!"
            return false
        }
        cadInputEmail.error = null

        if (cpf.isEmpty()) {
            cadInputCpf.error = "Insira um CPF!"
            return false
        } else if (!CPFUtil.validarCadastroCPF(cpf)){
            cadInputCpf.error = "CPF inválido!"
            Log.d("ERROR", "CPF inválido")
            return false
        }
        cadInputCpf.error = null

        if (metaVendas.isEmpty()) {
            cadInputMetaVendas.error = "Insira uma meta de vendas"
            return false
        }

        cadInputMetaVendas.error = null

        if (senha.isEmpty()) {
            cadInputSenha.error = "Insira uma senha!"
            return false
        }
        cadInputSenha.error = null

        if (imageBitMap == null) {
            textClickImgFunc.error = "Insira uma imagem!"
            return false
        }
        return true
    }

    private fun setNullFields() {
        cadInputNome.error = null
        cadInputEmail.error = null
        cadInputCpf.error = null
        cadInputTelefone.error = null
    }
}