package com.ifam.devm.appacai.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.senha.RecuperarSenhaViewModel
import com.ifam.devm.appacai.ui.startup.StartupActivity
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class AdminFragment : Fragment() {
    private lateinit var viewModel: RecuperarSenhaViewModel
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
        carregaNomeUsuarioDoBanco()
        super.onStart()
    }



    private fun carregaNomeUsuarioDoBanco() {
        doAsync {
            viewModel =
                RecuperarSenhaViewModel(AppDatabase.getDatabase(this@AdminFragment.requireContext()))
            viewModel.carregaDadosALTERAR()
            uiThread {
                usuario = viewModel.pegaDadosUsuario()
                try {
                    val sharedPreferences =
                        context?.getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
                    textonome.text =
                        sharedPreferences?.getString("nome", "")
                } catch (e: Exception) {
                    Toast.makeText(
                        this@AdminFragment.requireContext(),
                        "Nao foi possivel carregar o nome",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


/*
    override fun onStart() {
        carregaNomeUsuarioDoBanco()
        super.onStart()
    }

    private fun carregaNomeUsuarioDoBanco() {
        doAsync {
            viewModel =
                RecuperarSenhaViewModel(AppDatabase.getDatabase(this@HomeActivity))
            viewModel.carregaDadosALTERAR()
            uiThread {
                usuario = viewModel.pegaDadosUsuario()
                try {
                    val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
                    txtUsername.text =
                        sharedPreferences.getString("nome", "")
                } catch (e: Exception) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Nao foi possivel carregar o nome",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
*/
/*
        btLogOff.setOnClickListener {
            val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
            val sharedEditor = sharedPreferences.edit()
            sharedEditor.putString("login", "")
            sharedEditor.apply()
            startActivity(Intent(this@HomeActivity, StartupActivity::class.java))
            finishAffinity()
        }*/
