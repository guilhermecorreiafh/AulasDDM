package com.example.appdetarefas.model

/**
 * Model - representa uma tarefa a ser feita.
 */
data class Tarefa(
    val nome: String,
    val descricao: String,
    var concluida: Boolean = false
)
