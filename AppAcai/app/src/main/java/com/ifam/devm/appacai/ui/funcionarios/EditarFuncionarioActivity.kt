package com.ifam.devm.appacai.ui.funcionarios

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_editar_funcionario.*
import kotlinx.android.synthetic.main.activity_editar_funcionario.cadInputEmail
import kotlinx.android.synthetic.main.activity_editar_funcionario.cadInputNome
import kotlinx.android.synthetic.main.activity_editar_funcionario.cadInputTelefone
import kotlinx.android.synthetic.main.activity_editar_produto.*
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.*
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.view.*
import org.jetbrains.anko.doAsync

class EditarFuncionarioActivity : AppCompatActivity() {
    //funcionario
    private lateinit var funcionario : Funcionario

    //Atributos
    private var nome: String = ""
    private var email: String = ""
    private var telefone: String = ""
    private var cpf: String = ""
    private var metaVenda: Double = 0.0
    private var totalVenda: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_funcionario)

        toolbarEditarFuncionario.setOnClickListener{
            onBackPressed()
            finish()
        }

        val itens: Array<String> = arrayOf("tipo 1", "tipo 2", "tipo 3")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, itens)

        typesFilterSpinnerEdit.setAdapter(adapter)


        btSalvarFuncionario.setOnClickListener {
            if(verificarCampos()){
                funcionario.nome_funcionario = textInputNome.text.toString()
                funcionario.email_funcionario = textInputEmail.text.toString()
                funcionario.cpf_funcionario = textInputCPF.text.toString()
                funcionario.telefone_funcionario = textInputTelefone.text.toString()
                funcionario.meta_vendas = textInputMeta.text.toString() as Double
                funcionario.total_vendas = textInputTotal.text.toString() as Double


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
            val builder  = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            btAlertExcluir.setOnClickListener {
                val db = AppDatabase.getDatabase(this)
                doAsync {
                    db.funcionarioDao().delete(funcionario)
                }

                dialog.dismiss()

                val funcionarioJson = Gson().toJson(funcionario)
                val intentVisualizarFun = Intent(this, VisualizarFuncionarioActivity::class.java)
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

    private fun carregaDadosDoBanco(){
        val funcionarioJson = intent.getStringExtra("funcionario")
        val funcionario2 = Gson().fromJson(funcionarioJson, Funcionario::class.java)
        funcionario = funcionario2
        textInputNome.setText(funcionario.nome_funcionario)
        textInputEmail.setText(funcionario.email_funcionario)
        textInputTelefone.setText(funcionario.telefone_funcionario)
        textInputCPF.setText(funcionario.cpf_funcionario)
        textInputTotal.setText(funcionario.total_vendas as String)
        textInputMeta.setText(funcionario.meta_vendas as String)
    }

    fun verificarCampos(): Boolean{
        setCamposNull()

        nome = textInputNome.text.toString()
        email = textInputEmail.text.toString()
        telefone = textInputTelefone.text.toString()
        cpf = textInputCPF.text.toString()
        metaVenda = textInputMeta.text.toString() as Double
        totalVenda = textInputTotal.text.toString() as Double

        if(nome.isEmpty()){
            cadInputNome.error = "Por-favor insira um nome"
            return false
        }

        else if (email.isEmpty()){
            cadInputEmail.error =  "Por-favor informe um email"
            return false
        }

        else if(telefone.isEmpty()){
            cadInputTelefone.error = "Por-favor informe seu número de telefone"
            return false
        }
        else if(cpf.isEmpty()){
            cadInputCPF.error =  "Por-favor informe o seu CPF"
            return false
        }

        else if(metaVenda == 0.0){
            cadInputMetaVendas.error = "Por-favor insira sua meta de vendas"
            return false
        }

        else if(totalVenda == 0.0){
            cadInputTotal.error = "Por-favor insira seu total de vendas"
            return false
        }else {
            return true
        }


    }

    fun setCamposNull(){
        cadInputNome.error = null
        cadInputEmail.error = null
        cadInputTelefone.error = null
        cadInputCPF.error = null
        cadInputMeta.error = null
        cadInputTotal.error = null
    }
}