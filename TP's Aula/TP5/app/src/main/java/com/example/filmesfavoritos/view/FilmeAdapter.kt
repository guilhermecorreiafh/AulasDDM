package com.example.filmesfavoritos.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filmesfavoritos.R
import com.example.filmesfavoritos.databinding.ItemFilmeBinding
import com.example.filmesfavoritos.model.Filme

class FilmeAdapter(
    private val onEditar: (Filme) -> Unit,
    private val onRemover: (Filme) -> Unit
) : ListAdapter<Filme, FilmeAdapter.FilmeViewHolder>(Diff) {

    inner class FilmeViewHolder(private val binding: ItemFilmeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filme: Filme) {
            binding.textNome.text = filme.nome
            binding.textNota.text = "★ %.1f".format(filme.nota)
            binding.imagePoster.load(filme.foto) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_report_image)
            }
            binding.buttonEditar.setOnClickListener { onEditar(filme) }
            binding.buttonRemover.setOnClickListener { onRemover(filme) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object Diff : DiffUtil.ItemCallback<Filme>() {
        override fun areItemsTheSame(a: Filme, b: Filme) = a.id == b.id
        override fun areContentsTheSame(a: Filme, b: Filme) = a == b
    }
}
