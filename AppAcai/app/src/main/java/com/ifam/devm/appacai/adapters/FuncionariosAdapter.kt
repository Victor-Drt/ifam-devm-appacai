package com.ifam.devm.appacai.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Funcionario
import com.ifam.devm.appacai.ui.funcionarios.VisualizarFuncionarioActivity
import kotlinx.android.synthetic.main.dialog_confirmar_exclusao.view.*
import kotlinx.android.synthetic.main.linha_listar_vendedor.view.*
import kotlin.reflect.KFunction1

class FuncionariosAdapter(
    private val context: Context,
    private val listaFuncionarios: MutableList<Funcionario>,
    private val funcionarioClickLinha: KFunction1<Funcionario, Unit>,
    val editarClick: ((Funcionario) -> Unit),
    val excluirClick: ((Funcionario) -> Unit)
) : RecyclerView.Adapter<FuncionariosAdapter.FuncionariosViewHolder>() {

    override fun onBindViewHolder(holder: FuncionariosAdapter.FuncionariosViewHolder, position: Int) {
        val vendedor = listaFuncionarios[position]
        holder?.textNomeFuncionario?.text = vendedor.nome_funcionario
        holder?.textValorMeta?.text = vendedor.meta_vendas as String

       /* if (vendedor?.foto != null)
            holder?.imageFuncionario?.setImageBitmap(vendedor.foto) */

        holder?.btnConfig?.setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_opcoes_linha, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_editar -> {
                        //updateItem(position)
                        editarClick(listaFuncionarios[position])
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
    }

    private fun removerItem(position: Int) {
        excluirClick(listaFuncionarios[position])
        listaFuncionarios.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listaFuncionarios.size)
    }

    fun swapData(novaListaFuncionarios: List<Funcionario>){
        listaFuncionarios.clear()
        listaFuncionarios.addAll(novaListaFuncionarios)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionariosViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.linha_listar_vendedor, parent, false)
        return FuncionariosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaFuncionarios.size
    }

    /**
     * Classe ViewHolder que conterá cada item da lista de clientes
     */
    class FuncionariosViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val textNomeFuncionario: TextView = itemView.textNomeFuncionario
        val textValorMeta: TextView = itemView.textValorMeta
        val imageFuncionario: ImageView = itemView.imageFuncionario
        val btnConfig: ImageView = itemView.imageView2

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, VisualizarFuncionarioActivity::class.java)
//                intent.putExtra(view.context.getString(R.string.EXTRA_QR_CODE), nomeCliente.toQRString())
                itemView.context.startActivity(intent)
            }
        }
    }
}
