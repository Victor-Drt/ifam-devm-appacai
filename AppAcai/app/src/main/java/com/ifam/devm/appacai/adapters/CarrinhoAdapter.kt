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
import com.ifam.devm.appacai.model.ProdutoVenda
import kotlinx.android.synthetic.main.linha_confirm_prods.view.*
import kotlin.reflect.KFunction1

class CarrinhoAdapter(
    private val context: Context,
    private val listaProdutosVenda : MutableList<ProdutoVenda>,
    private val itemNexCallBack: KFunction1<ProdutoVenda, Unit>,
    ) : RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.linha_confirm_prods, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarrinhoAdapter.ViewHolder, position: Int) {
        val produtoVenda = listaProdutosVenda[position]

//        setando campos da linha item
        holder?.campoNomeProduto?.text = produtoVenda.nome

//        configurando valor
        var tvValor = produtoVenda.valor.toString()
        tvValor = tvValor.replace('.', ',')
        if (tvValor.length - tvValor.indexOf(',') < 3)
            tvValor += '0'
        holder?.campoValorProduto?.text = tvValor

        if (produtoVenda?.foto != null) {
            var fotoproduto = BitmapFactory.decodeByteArray(produtoVenda.foto, 0, (produtoVenda.foto)?.size!!)
            holder?.imageProduto?.setImageBitmap(fotoproduto)
        }

        holder?.btnExclui?.setOnClickListener {
            itemNexCallBack(produtoVenda)
        }
    }

    override fun getItemCount(): Int {
        return listaProdutosVenda.size
    }

    fun swapData(novaListaProdutosVenda: List<ProdutoVenda>) {
        listaProdutosVenda.clear()
        listaProdutosVenda.addAll(novaListaProdutosVenda)
        notifyDataSetChanged()
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder (itemView) {
        val campoNomeProduto: TextView = itemView.textNomeProdutoConfirm
        val campoValorProduto: TextView = itemView.textValorProdutoConfirm
        val btnExclui: ImageView = itemView.imageCancelProd
        val imageProduto: ImageView = itemView.imageProdutoConfirm
    }
}