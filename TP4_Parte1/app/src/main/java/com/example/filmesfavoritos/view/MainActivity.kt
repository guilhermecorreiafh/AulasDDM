package com.example.filmesfavoritos.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.filmesfavoritos.R
import com.example.filmesfavoritos.dao.FilmeAdapter
import com.example.filmesfavoritos.model.Filme

/**
 * View - tela principal que exibe a lista de filmes favoritos
 * em um RecyclerView usando GridLayoutManager (2 colunas).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FilmeAdapter
    private val listaFilmes = mutableListOf<Filme>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewFilmes)
        // Diferente tipo de LayoutManager: Grid, com 2 colunas
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = FilmeAdapter(listaFilmes)
        recyclerView.adapter = adapter

        val fabAdicionar = findViewById<FloatingActionButton>(R.id.fabAdicionarFilme)
        fabAdicionar.setOnClickListener {
            exibirDialogNovoFilme()
        }
    }

    private fun exibirDialogNovoFilme() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_novo_filme, null)

        val etTitulo = dialogView.findViewById<EditText>(R.id.etTitulo)
        val etDiretor = dialogView.findViewById<EditText>(R.id.etDiretor)

        AlertDialog.Builder(this)
            .setTitle("Novo filme favorito")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                val diretor = etDiretor.text.toString().trim()

                if (titulo.isNotEmpty() && diretor.isNotEmpty()) {
                    adapter.adicionarFilme(Filme(titulo, diretor))
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
