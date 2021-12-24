package com.ifam.devm.appacai.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.ProdutosAdapter
import com.ifam.devm.appacai.model.Produto
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoActivity
import com.ifam.devm.appacai.ui.cadastro_produto.CadastrarProdutoViewModel
import com.ifam.devm.appacai.ui.cadastro_produto.EditarProdutoActivity
import com.ifam.devm.appacai.ui.cadastro_produto.VisualizarProdutoActivity
import kotlinx.android.synthetic.main.fragment_produtos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import com.ifam.devm.appacai.ui.funcionarios.EditarFuncionarioActivity
import com.ifam.devm.appacai.ui.funcionarios.VisualizarFuncionarioActivity


class ProdutosFragment : Fragment() {

    private lateinit var produtosCadastrados: MutableList<Produto>
    private lateinit var produtosAdapter: ProdutosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_produtos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMenuVisibility(true)

        fbAddProduto.setOnClickListener {
            val act = activity
            if (act != null) {
                startActivity(Intent(act, CadastrarProdutoActivity::class.java))
            }
        }

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                // quando algo for submetido no campo de buscar essa função é executada
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("debuga", "funcionou")
                    if (query != null) {
                        searchDatabase(query)
                    }
                    return true
                }

                // quando algo é digitado no campo de buscar essa função é executada
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

    //função utilizada pelo searchView para filtrar a lista de itens cadastrados pelo o que
    // foi procurado

    private fun searchDatabase(query: String) {
        if (query.isEmpty()) {
            produtosAdapter.swapData(produtosCadastrados)
        } else {
            var listaFiltrada = produtosCadastrados.filter {
                it.nome.toUpperCase().contains(query.toUpperCase())
            }
            if (listaFiltrada.isEmpty()) {
                listaFiltrada = produtosCadastrados.filter {
                    it.descricao.toUpperCase().contains(query.toUpperCase())
                }
            }
            produtosAdapter.swapData(listaFiltrada)
        }
    }

    override fun onResume() {
        super.onResume()
        // inicializa RecyclerView
        produtosAdapter = ProdutosAdapter(
            this@ProdutosFragment.requireContext(),
            mutableListOf(),
            ::onClickItem,
            ::editarItemClick,
            ::deleteItemClick
        )

        rvListaProdutos.adapter = produtosAdapter
        rvListaProdutos.layoutManager = LinearLayoutManager(this@ProdutosFragment.requireContext())

//        Inicializa viewModel e obtém lista inicial de produtos
        doAsync {
            val sharedPreferences = activity?.getSharedPreferences(
                PREF_DATA_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
            val produtosViewModel =
                CadastrarProdutoViewModel(AppDatabase.getDatabase(this@ProdutosFragment.requireContext()))

            produtosCadastrados = produtosViewModel.getAllProduto() as MutableList<Produto>
            Log.d("M", produtosCadastrados.toString())

            uiThread {
                produtosAdapter.swapData(produtosCadastrados)
            }
        }
    }

    private fun onClickItem(produto: Produto) {
        val intent = Intent(this@ProdutosFragment.requireContext(), VisualizarProdutoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        startActivity(intent)
    }

    private fun deleteItemClick(produto: Produto) {
        //entra em contato com o banco de dados e exclui o produto
        val data = AppDatabase.getDatabase(this@ProdutosFragment.requireContext())
        doAsync {
            data.produtoDao().delete(produto)
        }
    }

    private fun editarItemClick(produto: Produto) {
        //cria uma intent enviando as informaçoes do produto para a prox tela
        val intent = Intent(this@ProdutosFragment.requireContext(), EditarProdutoActivity::class.java)
        intent.putExtra("produto_nome", produto.nome)
        startActivity(intent)
    }

}
