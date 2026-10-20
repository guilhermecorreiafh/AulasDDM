package com.example.filmesfavoritos.model

/**
 * MODEL: fonte de dados em memória (poderia ser trocada por Room/API sem mexer na View).
 */
class FilmeRepository {

    private var proximoId = 1L
    private val filmes = mutableListOf<Filme>()

    init {
        adicionar("Interestelar", 9.5f, "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg")
        adicionar("O Poderoso Chefão", 9.8f, "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg")
        adicionar("Parasita", 9.0f, "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg")
    }

    fun listar(): List<Filme> = filmes.toList()

    fun adicionar(nome: String, nota: Float, foto: String) {
        filmes.add(Filme(proximoId++, nome, nota, foto))
    }

    fun editar(id: Long, nome: String, nota: Float, foto: String) {
        val index = filmes.indexOfFirst { it.id == id }
        if (index >= 0) filmes[index] = filmes[index].copy(nome = nome, nota = nota, foto = foto)
    }

    fun remover(id: Long) {
        filmes.removeAll { it.id == id }
    }
}
