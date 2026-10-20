package com.example.filmesfavoritos.model

/**
 * MODEL: representa um filme favorito.
 * [foto] guarda a URL (https://...) ou a Uri (content://...) da imagem, carregada com Coil.
 */
data class Filme(
    val id: Long,
    val nome: String,
    val nota: Float,
    val foto: String
)
