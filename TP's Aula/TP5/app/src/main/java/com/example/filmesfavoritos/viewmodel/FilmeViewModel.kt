package com.example.filmesfavoritos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmesfavoritos.model.Filme
import com.example.filmesfavoritos.model.FilmeRepository

/**
 * VIEWMODEL: guarda o estado da tela e a lógica de edição da lista.
 * A View apenas observa [filmes] e [erro] e chama as funções abaixo.
 */
class FilmeViewModel : ViewModel() {

    private val repository = FilmeRepository()

    private val _filmes = MutableLiveData<List<Filme>>(repository.listar())
    val filmes: LiveData<List<Filme>> = _filmes

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    fun salvar(id: Long?, nome: String, notaTexto: String, foto: String) {
        val nomeLimpo = nome.trim()
        val nota = notaTexto.replace(',', '.').toFloatOrNull()

        when {
            nomeLimpo.isEmpty() -> { _erro.value = "Informe o nome do filme"; return }
            nota == null || nota < 0f || nota > 10f -> { _erro.value = "A nota deve estar entre 0 e 10"; return }
        }

        if (id == null) repository.adicionar(nomeLimpo, nota!!, foto.trim())
        else repository.editar(id, nomeLimpo, nota!!, foto.trim())

        _filmes.value = repository.listar()
    }

    fun remover(filme: Filme) {
        repository.remover(filme.id)
        _filmes.value = repository.listar()
    }

    fun erroConsumido() {
        _erro.value = null
    }
}
