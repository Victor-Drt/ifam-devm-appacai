package com.ifam.devm.appacai.ui.home

//import com.ifam.devm.appacai.adapters.FuncionariosAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.FuncionariosAdapter
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.funcionarios.FuncionariosCadastrar
import com.ifam.devm.appacai.ui.funcionarios.VisualizarFuncionarioActivity
import kotlinx.android.synthetic.main.fragment_produtos.*
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

        fbAddItem.setOnClickListener {
            startActivity(
                Intent(
                    this@VendedoresFragment.requireContext(),
                    FuncionariosCadastrar::class.java
                )
            )
        }

        searchViewFuncionarios.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                // quando algo for submetido no campo de buscar essa fun????o ?? executada
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("debuga", "funcionou")
                    if (query != null) {
                        searchDatabase(query)
                    }
                    return true
                }

                // quando algo ?? digitado no campo de buscar essa fun????o ?? executada
                override fun onQueryTextChange(query: String?): Boolean {
                    if (query != null) {
                        Log.d("debuga", query)
                    }
                    if (query != null) {
                        searchDatabase(query)
                    }
                    return true
                }
            })
    }

    /************************** Metodos para a pesquisa de itens ***************************/

    //fun????o utilizada pelo searchView para filtrar a lista de itens cadastrados pelo o que
    // foi procurado

    private fun searchDatabase(query: String) {
        if (query.isEmpty()) {
            funcionariosAdapter.swapData(funcionariosCadastrados)
        } else {
            var listaFiltrada = funcionariosCadastrados.filter {
                it.nome_funcionario.toUpperCase().contains(query.toUpperCase())
            }
            if (listaFiltrada.isEmpty()) {
                listaFiltrada = funcionariosCadastrados.filter {
                    it.email_funcionario.toUpperCase().contains(query.toUpperCase())
                }
            }
            funcionariosAdapter.swapData(listaFiltrada)
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
        rvListaFuncionarios.layoutManager =
            LinearLayoutManager(this@VendedoresFragment.requireContext())

        //Inicializa viewModel e obt??m lista inicial de clientes
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val funcionariosViewModel =
                CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VendedoresFragment.requireContext()))
            funcionariosCadastrados =
                funcionariosViewModel.getAllFuncionarios() as MutableList<Funcionario>
            Log.d("F", funcionariosCadastrados.toString())

            uiThread {
                funcionariosAdapter.swapData(funcionariosCadastrados)
            }
        }
    }

    private fun onClickItem(funcionario: Funcionario) {
        val intent = Intent(this@VendedoresFragment.requireContext(), VisualizarFuncionarioActivity::class.java)
        intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
        startActivity(intent)
    }

    private fun deleteItemClick(funcionario: Funcionario) {
        val data = AppDatabase.getDatabase(this@VendedoresFragment.requireContext())
        doAsync {
            data.funcionarioDao().delete(funcionario)
        }
    }

    private fun editarItemClick(funcionario: Funcionario) {
        val intent = Intent(this@VendedoresFragment.requireContext(), EditarFuncionarioActivity::class.java)
        intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
        startActivity(intent)

    }
}