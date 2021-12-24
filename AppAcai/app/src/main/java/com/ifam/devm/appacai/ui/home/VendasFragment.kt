package com.ifam.devm.appacai.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.FuncionariosAdapter
import com.ifam.devm.appacai.adapters.ProdutosAdapter
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoViewModel
import com.ifam.devm.appacai.ui.cadastro_produto.EditarProdutoActivity
import com.ifam.devm.appacai.ui.cadastro_produto.VisualizarProdutoActivity
import com.ifam.devm.appacai.ui.funcionarios.CadastrarFuncionarioViewModel
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.funcionarios.VisualizarFuncionarioActivity
import kotlinx.android.synthetic.main.fragment_produtos.*
import kotlinx.android.synthetic.main.fragment_vendas.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class VendasFragment : Fragment() {

    private lateinit var produtosCadastrados: MutableList<Produto>
    private lateinit var produtosAdapter: ProdutosAdapter

    private lateinit var funcionariosCadastrados: MutableList<Funcionario>
    private lateinit var funcionariosAdapter: FuncionariosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


// inicializa RecyclerView Produtos
        produtosAdapter = ProdutosAdapter(
            this@VendasFragment.requireContext(),
            mutableListOf(),
            ::onClickItem,
            ::editarItemClick,
            ::deleteItemClick
        )

        rvProdutosVendasFragment.adapter = produtosAdapter
        rvProdutosVendasFragment.layoutManager =
            LinearLayoutManager(this@VendasFragment.requireContext())

//        Inicializa viewModel e obtém lista inicial de produtos
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val produtosViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@VendasFragment.requireContext()))

            produtosCadastrados = produtosViewModel.getAllProduto() as MutableList<Produto>
            Log.d("M", produtosCadastrados.toString())

            uiThread {
                produtosAdapter.swapData(produtosCadastrados.sortedWith(compareByDescending({ it.freqVenda })))
            }
        }

        // inicializa RecyclerView
        funcionariosAdapter = FuncionariosAdapter(
            this@VendasFragment.requireContext(),
            mutableListOf(),
            ::onClickItemVendedor,
            ::editarItemVendedorClick,
            ::deleteItemVendedorClick,
        )

        rvVendedoresVendasFragment.adapter = funcionariosAdapter
        rvVendedoresVendasFragment.layoutManager =
            LinearLayoutManager(this@VendasFragment.requireContext())

//        Inicializa viewModel e obtém lista inicial de produtos
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val funcionariosViewModel =
                CadastrarFuncionarioViewModel(AppDatabase.getDatabase(this@VendasFragment.requireContext()))

            funcionariosCadastrados =
                funcionariosViewModel.getAllFuncionarios() as MutableList<Funcionario>
            Log.d("M", funcionariosCadastrados.toString())

            uiThread {
                funcionariosAdapter.swapData(funcionariosCadastrados.sortedWith(compareByDescending(
                    { it.total_vendas })))
            }
        }
    }

    //    Funcionarios
    private fun onClickItemVendedor(funcionario: Funcionario) {
        val intent = Intent(this@VendasFragment.requireContext(), VisualizarFuncionarioActivity::class.java)
        intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
        startActivity(intent)
    }

    private fun deleteItemVendedorClick(funcionario: Funcionario) {
        val data = AppDatabase.getDatabase(this@VendasFragment.requireContext())
        doAsync {
            data.funcionarioDao().delete(funcionario)
        }
    }

    private fun editarItemVendedorClick(funcionario: Funcionario) {
        val intent = Intent(this@VendasFragment.requireContext(), EditarFuncionarioActivity::class.java)
        intent.putExtra("funcionario_nome", funcionario.nome_funcionario)
        startActivity(intent)
    }

    //    Produtos
    private fun onClickItem(produto: Produto) {
        val intent = Intent(this@VendasFragment.requireContext(), VisualizarProdutoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        startActivity(intent)
    }

    private fun deleteItemClick(produto: Produto) {
        //entra em contato com o banco de dados e exclui o produto
        val data = AppDatabase.getDatabase(this@VendasFragment.requireContext())
        doAsync {
            data.produtoDao().delete(produto)
        }
    }

    private fun editarItemClick(produto: Produto) {
        //cria uma intent enviando as informaçoes do produto para a prox tela
        val intent = Intent(this@VendasFragment.requireContext(), EditarProdutoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        startActivity(intent)
    }

}
