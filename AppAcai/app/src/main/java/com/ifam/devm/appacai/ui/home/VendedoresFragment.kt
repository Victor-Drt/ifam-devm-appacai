package com.ifam.devm.appacai.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.FuncionariosAdapter
import com.ifam.devm.appacai.model.Funcionario
//import com.ifam.devm.appacai.adapters.FuncionariosAdapter
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.funcionarios.VisualizarFuncionarioActivity
import com.ifam.devm.appacai.ui.funcionarios.funcionariosCadastrar
import kotlinx.android.synthetic.main.fragment_vendedores.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class VendedoresFragment : Fragment() {

    private lateinit var funcionariosCadastrados: MutableList<Funcionario>
    private lateinit var funcionariosAdapter: FuncionariosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendedores, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         //TODO descomentar depois de fazer as activitys
        fbAddItem.setOnClickListener {
            startActivity(Intent(this@VendedoresFragment.requireContext(), funcionariosCadastrar::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        // inicializa RecyclerView
        funcionariosAdapter = FuncionariosAdapter(
            this@VendedoresFragment.requireContext(),
            mutableListOf(),
            ::onClickItem,
            ::editarItemClick,
            ::deleteItemClick,
        )

        rvListaFuncionarios.adapter = funcionariosAdapter
        rvListaFuncionarios.layoutManager = LinearLayoutManager(this@VendedoresFragment.requireContext())

        //Inicializa viewModel e obtém lista inicial de clientes
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val funcionariosViewModel = CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VendedoresFragment.requireContext()))
            funcionariosCadastrados = funcionariosViewModel.getAllFuncionarios() as MutableList<Funcionario>
            Log.d("F", funcionariosCadastrados.toString())

            uiThread {
                funcionariosAdapter.swapData(funcionariosCadastrados)
            }
        }
    }

    private fun onClickItem(funcionario: Funcionario){
        val funcionarioJSON = Gson().toJson(funcionario)
        val intentEditarFuncionario = Intent(this@VendedoresFragment.requireContext(), VisualizarFuncionarioActivity::class.java)
        intentEditarFuncionario.putExtra("funcionario", funcionarioJSON)
        startActivity(intentEditarFuncionario)
    }

    private fun deleteItemClick(funcionario: Funcionario){
        val data = AppDatabase.getDatabase(this@VendedoresFragment.requireContext())
        doAsync {
            data.funcionarioDao().delete(funcionario)
        }
    }

    private fun editarItemClick(funcionario: Funcionario){
        val funcionarioJson = Gson().toJson(funcionario)
        val intentEditarFuncionario = Intent(this@VendedoresFragment.requireContext(), EditarFuncionarioActivity::class.java)
        intentEditarFuncionario.putExtra("funcionario", funcionarioJson)
        startActivity(intentEditarFuncionario)

    }
}