package com.ifam.devm.appacai.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.UserViewModel
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_user.EditarDadosUserActivity
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.startup.StartupActivity
import com.ifam.devm.appacai.utils.Mask
import com.ifam.devm.appacai.utils.MaskCopy.MaskChangedListener
import com.ifam.devm.appacai.utils.MaskCopy.MaskSL
import com.ifam.devm.appacai.utils.MaskCopy.MaskStyle
import kotlinx.android.synthetic.main.acitivity_cadastra_funcionario.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_visualizar_funcionario.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class AdminFragment : Fragment() {
    private lateinit var viewModel: UserViewModel
    private lateinit var usuario: Usuario

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarDadosLoja.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editar_dados -> {
                    val intent = Intent(
                        this@AdminFragment.requireContext(),
                        EditarDadosUserActivity::class.java
                    )
                    intent.putExtra("user_name", txtNomeVisualizar.text.toString())
                    startActivity(intent)
                }

                R.id.sobre_app -> {
                    val intent =
                        Intent(this@AdminFragment.requireContext(), SobreOAppActivity::class.java)
                    intent.putExtra("user_name", txtNomeVisualizar.text.toString())
                    startActivity(intent)
                }
            }
            true
        }

        btLogOff.setOnClickListener {
            val sharedPreferences =
                this.activity?.getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
            val sharedEditor = sharedPreferences?.edit()
            sharedEditor?.putString("login", "")
            sharedEditor?.apply()
            val act = activity
            if (act != null) {
                startActivity(Intent(act, StartupActivity::class.java))
            }

        }

    }

    override fun onStart() {
        carregaDadosDoBanco()
        super.onStart()
    }

    private fun carregaDadosDoBanco() {
        doAsync {
            viewModel =
                UserViewModel(AppDatabase.getDatabase(this@AdminFragment.requireContext()))
            viewModel.CarregaDadosUsuario()
            uiThread {
                usuario = viewModel.getTodosDadosUsuario()
                if (usuario?.foto != null) {
                    var fotoQr =
                        BitmapFactory.decodeByteArray(usuario.foto, 0, (usuario.foto)?.size!!)
                    imageQRVisu?.setImageBitmap(fotoQr)
                }
                println(usuario.nomeUsuario)
                println(usuario.email)
                txtNomeVisualizar.setText(usuario.nomeUsuario)
                txtNomeFantasiaVisualizar.setText(usuario.nomeEmpresa)
                txtEmailVisualizar.setText(usuario.email)
                txtPixVisualizar.setText(usuario.chavePix)
            }
        }
    }
}