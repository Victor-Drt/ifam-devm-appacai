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
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import kotlinx.android.synthetic.main.fragment_vendedores.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class VendedoresFragment : Fragment() {

//    private lateinit var funcionariosCadastrados: MutableList<Funcionario>
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

        /*
         TODO descomentar depois de fazer as activitys
        fbAddItem.setOnClickListener {
            startActivity(Intent(this@VendedoresFragment.requireContext(), CadastrarFuncionarioActivity::class.java))
        }

        // inicializa RecyclerView
        funcionariosAdapter = FuncionariosAdapter(
            this,
            mutableListOf(),
            ::editarItemClick,
            ::deleteItemClick
        )

        rvListaFuncionarios.adapter = funcionariosAdapter
        rvListaFuncionarios.layoutManager = LinearLayoutManager(this@VendedoresFragment.requireContext())

        //Inicializa viewModel e obtém lista inicial de clientes
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val cpfFuncionario = sharedPreferences?.getString("cpf","0").toString()
            funcioanriosViewModel = FuncioanriosViewModel(AppDatabase.getDatabase(applicationContext))
            funcionariosCadastrados = funcioanriosViewModel.getAllClientes() as MutableList<Funcionario>
            Log.d("M",funcionariosCadastrados.toString())
            //filtra a lista para exibir somente aquilo cadastrado pelo usuário logado
            uiThread {
                Log.d("M",cpfCliente)
                funcionariosCadastrados = funcionariosCadastrados.filter { it.usuario == cpfCliente } as MutableList<Funcionario>
                Log.d("M",funcionariosCadastrados.toString())
                // a lista gerada é exibida
                funcionariosAdapter.swapData(funcionariosCadastrados.sortedWith(compareBy {
                    it.nome.toUpperCase(Locale.ROOT)
                }))
            }
        }

        //quando botao editar cliente é clicados
        private fun editarItemClick(funcionario: Funcionario) {
            //cria uma intent enviando as informaçoes do cliente para a prox tela
            val clienteJson = Gson().toJson(funcionario)
            val intentEditarFuncionario =
                Intent(this@VendedoresFragment.requireContext(), EditarFuncionarioActivity::class.java)
            intentEditarFuncionario.putExtra("funcionario", clienteJson)
            startActivity(intentEditarFuncionario)
        }

        //quando o botao excluir cliente é clicados
        private fun deleteItemClick(funcionario: Funcionario) {
            //entra em contato com o banco de dados e exclui o cliente
            val data = AppDatabase.getDatabase(this@VendedoresFragment.requireContext())
            doAsync {
                data.funcionarioDao().delete(funcionario)
            }
        }
        */
    }

}