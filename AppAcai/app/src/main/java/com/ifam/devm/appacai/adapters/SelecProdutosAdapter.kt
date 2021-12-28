package com.ifam.devm.appacai.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import kotlinx.android.synthetic.main.linha_selec_prods.view.*
import kotlin.reflect.KFunction1

class SelecProdutosAdapter(
    private val context: Context,
    private val listaProdutos: MutableList<Produto>,
    private val itemNexCallBack: KFunction1<Produto, Unit>,
    ) : RecyclerView.Adapter<SelecProdutosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.linha_selec_prods, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelecProdutosAdapter.ViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder?.campoNomeProduto?.text = produto.nome
        holder?.campoDescProduto?.text = produto.descricao

        //        configurando valor
        var tvValor = produto.valor.toString()
        tvValor = tvValor.replace('.', ',')
        if (tvValor.length - tvValor.indexOf(',') < 3)
            tvValor += '0'

        holder?.campoValorProduto?.text = tvValor

        if (produto?.foto != null) {
            var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
            holder?.imageProduto?.setImageBitmap(fotoproduto)
        }

        holder?.btnAdd?.setOnClickListener {
            itemNexCallBack(produto)
        }
    }

    override fun getItemCount(): Int {
        return listaProdutos.size
    }

    fun swapData(novaListaProdutos: List<Produto>) {
        listaProdutos.clear()
        listaProdutos.addAll(novaListaProdutos)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ){
        val campoNomeProduto: TextView = itemView.textNomeProdutoSelec
        val campoDescProduto: TextView = itemView.textDescricaoProdutoSelec
        val campoValorProduto: TextView = itemView.textValorProdutoSelec
        val btnAdd: ImageView = itemView.imageAddProd
        val imageProduto: ImageView = itemView.imageProdutoSelec
    }
}