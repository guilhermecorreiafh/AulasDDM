package com.example.tp7.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp7.R
import com.example.tp7.model.Item
import java.text.NumberFormat
import java.util.Locale

class ItemAdapter(
    private val itens: MutableList<Item>,
    private val idsSelecionados: Set<String> = emptySet(),
    private val aoAlterarSelecao: ((Item, Boolean) -> Boolean)? = null
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.txtNomeItem)
        val restaurante: TextView = itemView.findViewById(R.id.txtRestauranteItem)
        val preco: TextView = itemView.findViewById(R.id.txtPrecoItem)
        val checkbox: CheckBox = itemView.findViewById(R.id.chkItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardapio, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itens[position]
        holder.nome.text = item.nome
        holder.restaurante.text = "Restaurante: ${item.restauranteNome}"
        holder.preco.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(item.preco)

        holder.checkbox.setOnCheckedChangeListener(null)
        if (aoAlterarSelecao == null) {
            holder.checkbox.visibility = View.GONE
        } else {
            holder.checkbox.visibility = View.VISIBLE
            holder.checkbox.isChecked = idsSelecionados.contains(item.id)
            holder.checkbox.setOnCheckedChangeListener { button, marcado ->
                if (aoAlterarSelecao.invoke(item, marcado).not()) {
                    button.isChecked = false
                }
            }
        }
    }

    override fun getItemCount(): Int = itens.size
}
