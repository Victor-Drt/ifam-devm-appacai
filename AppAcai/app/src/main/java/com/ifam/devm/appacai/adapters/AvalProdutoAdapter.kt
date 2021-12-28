package com.ifam.devm.appacai.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import kotlinx.android.synthetic.main.linha_aval_produto.view.*
import kotlinx.android.synthetic.main.linha_selec_prods.view.*
import kotlin.reflect.KFunction1

class AvalProdutoAdapter(
    private val context: Context,
    private val listaProdutos: MutableList<Produto>,
    private val produtoClickLinha: KFunction1<Produto, Unit>,
) : RecyclerView.Adapter<AvalProdutoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.linha_aval_produto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvalProdutoAdapter.ViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder?.campoNomeProduto?.text = produto.nome
        //        configurando valor
        var tvValor = produto.valor.toString()
        tvValor = tvValor.replace('.', ',')
        if (tvValor.length - tvValor.indexOf(',') < 3)
            tvValor += '0'
        holder?.campoValorProduto?.text = tvValor

        holder?.ratingBarAvalProd.rating = (produto.avaliacao)/produto.qtdVotos

        if (produto?.foto != null) {
            var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
            holder?.imageProduto?.setImageBitmap(fotoproduto)
        }

        holder.rowItem.setOnClickListener {
            produtoClickLinha(produto)
        }
    }

    fun swapData(novaListaProdutos: List<Produto>) {
        listaProdutos.clear()
        listaProdutos.addAll(novaListaProdutos)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listaProdutos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ){
        val campoNomeProduto: TextView = itemView.textNomeProdutoAval
        val campoValorProduto: TextView = itemView.textValorProdutoAval
        val ratingBarAvalProd: RatingBar = itemView.ratingBarAvalProd
        val imageProduto: ImageView = itemView.imageProdutoAval
        val rowItem : CardView = itemView.linhaProdutoAval
    }
}