package com.ifam.devm.appacai.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Produto
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.view.*
import kotlinx.android.synthetic.main.linha_listar_produto.view.*
import java.io.ByteArrayInputStream
import kotlin.reflect.KFunction1

class ProdutosAdapter(
    private val context: Context,
    private val listaProdutos: MutableList<Produto>,
    private val produtoClickLinha: KFunction1<Produto, Unit>,
    val editarClick: ((Produto) -> Unit),
    val excluirClick: ((Produto) -> Unit)
) : RecyclerView.Adapter<ProdutosAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ProdutosAdapter.ViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder?.campoNomeProduto?.text = produto.nome
        holder?.campoDescProduto?.text = produto.descricao
        holder?.campoValorProduto?.text = produto.valor.toString()

        if (produto?.foto != null) {
            var fotoproduto = BitmapFactory.decodeByteArray(produto.foto, 0, (produto.foto)?.size!!)
            holder?.imageProduto?.setImageBitmap(fotoproduto)
        }

        holder?.btnConfig?.setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_opcoes_linha, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_editar -> {
                        //updateItem(position)
                        editarClick(listaProdutos[position])
                        true
                    }
                    R.id.menu_excluir -> {
                        //inicia dialog view quando clicado
                        val view =
                            View.inflate(context, R.layout.dialog_confirmar_exclusao, null)
                        val builder = AlertDialog.Builder(context)
                        builder.setView(view)

                        //mostra alert dialog para validar o sair sem salvar ou não
                        val dialog = builder.create()
                        dialog.show()

                        view.btAlertExcluir.setOnClickListener {
                            //entra em contato com o banco de dados e exclui o cliente
                            removerItem(position)
                            dialog.dismiss()
                        }
                        view.btAlertCancelarRemover.setOnClickListener {
                            dialog.dismiss()
                        }
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        holder.rowItem.setOnClickListener {
            produtoClickLinha(produto)
        }
    }

    private fun removerItem(position: Int) {
        excluirClick(listaProdutos[position])
        listaProdutos.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listaProdutos.size)
    }

    fun swapData(novaListaProdutos: List<Produto>) {
        listaProdutos.clear()
        listaProdutos.addAll(novaListaProdutos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.linha_listar_produto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaProdutos.size
    }

    /**
     * Classe ViewHolder que conterá cada item da lista de clientes
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val campoNomeProduto: TextView = itemView.textNomeProduto
        val campoDescProduto: TextView = itemView.textDescricaoProduto
        val campoValorProduto: TextView = itemView.textValorProduto
        val btnConfig: ImageView = itemView.imageOptionsProd
        val imageProduto: ImageView = itemView.imageProduto

        val rowItem : CardView = itemView.linhaProduto

//        init {
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, VisualizarProdutoActivity::class.java)
////                intent.putExtra(view.context.getString(R.string.EXTRA_QR_CODE), nomeCliente.toQRString())
//                itemView.context.startActivity(intent)
//            }
//        }
    }
}