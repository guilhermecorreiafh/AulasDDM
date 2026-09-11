package com.example.myapplication.dao

import com.example.myapplication.model.Imc

class ImcDao {

    companion object {
        var imc: Imc? = null
    }

    fun calcular(peso: Double, altura: Double) {

        val valorImc = peso / (altura * altura)

        val classificacao = when {
            valorImc < 18.5 -> "Abaixo do peso"
            valorImc < 25.0 -> "Peso ideal"
            valorImc < 30.0 -> "Sobrepeso"
            else -> "Obesidade"
        }

        imc = Imc(
            peso,
            altura,
            valorImc,
            classificacao
        )
    }

    fun buscar(): Imc? {
        return imc
    }
}