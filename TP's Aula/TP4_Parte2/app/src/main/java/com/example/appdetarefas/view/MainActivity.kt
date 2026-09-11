package com.example.appdetarefas.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.appdetarefas.R
import com.example.appdetarefas.dao.TarefaAdapter
import com.example.appdetarefas.model.Tarefa

/**
 * View - tela principal que exibe a lista de tarefas
 * em um RecyclerView usando LinearLayoutManager.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TarefaAdapter
    private val listaTarefas = mutableListOf<Tarefa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewTarefas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TarefaAdapter(listaTarefas)
        recyclerView.adapter = adapter

        val fabAdicionar = findViewById<FloatingActionButton>(R.id.fabAdicionarTarefa)
        fabAdicionar.setOnClickListener {
            exibirDialogNovaTarefa()
        }
    }

    private fun exibirDialogNovaTarefa() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_nova_tarefa, null)

        val etNome = dialogView.findViewById<EditText>(R.id.etNome)
        val etDescricao = dialogView.findViewById<EditText>(R.id.etDescricao)

        AlertDialog.Builder(this)
            .setTitle("Nova tarefa")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val nome = etNome.text.toString().trim()
                val descricao = etDescricao.text.toString().trim()

                if (nome.isNotEmpty()) {
                    adapter.adicionarTarefa(Tarefa(nome, descricao))
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
