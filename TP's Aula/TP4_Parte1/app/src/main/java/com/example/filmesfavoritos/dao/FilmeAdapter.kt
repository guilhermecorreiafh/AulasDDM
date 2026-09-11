package com.example.filmesfavoritos.dao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmesfavoritos.R
import com.example.filmesfavoritos.model.Filme

/**
 * "DAO" / Adapter - responsável por gerenciar a lista de filmes
 * e conectar os dados (Model) com o RecyclerView (View).
 */
class FilmeAdapter(
    private val filmes: MutableList<Filme>
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    class FilmeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDiretor: TextView = itemView.findViewById(R.id.tvDiretor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filme, parent, false)
        return FilmeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = filmes[position]
        holder.tvTitulo.text = filme.titulo
        holder.tvDiretor.text = filme.diretor
    }

    override fun getItemCount(): Int = filmes.size

    /** Adiciona um novo filme à lista e atualiza o RecyclerView. */
    fun adicionarFilme(filme: Filme) {
        filmes.add(filme)
        notifyItemInserted(filmes.size - 1)
    }
}
