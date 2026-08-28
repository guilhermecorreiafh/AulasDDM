package com.example.myapplication.dao

import com.example.myapplication.model.Aluno
import kotlin.random.Random

class AlunoDao {

    companion object {
        var aluno: Aluno? = null
    }

    fun salvar(nome: String, turma: String) {
        aluno = Aluno(nome, turma)
    }

    fun buscar(): Aluno? {
        return aluno
    }

    fun gerarMatricula(): Int {
        return Random.nextInt(100000, 999999)
    }
}