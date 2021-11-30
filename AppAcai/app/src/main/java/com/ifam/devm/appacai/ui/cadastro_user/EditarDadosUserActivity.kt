package com.ifam.devm.appacai.ui.cadastro_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.repository.UserViewModel
import kotlinx.android.synthetic.main.activity_editar_dados_user.*

class EditarDadosUserActivity : AppCompatActivity() {

    private lateinit var nome: String
    private lateinit var nomeEmpresa: String
    private lateinit var email: String
    private lateinit var pix: String
    private lateinit var senha: String
    private lateinit var senha_confirmar: String

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_dados_user)

        toolbarDadosLojaEditar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }
}