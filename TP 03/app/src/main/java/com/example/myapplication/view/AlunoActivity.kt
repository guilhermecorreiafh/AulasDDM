package com.example.myapplication.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.dao.AlunoDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlunoActivity : AppCompatActivity(R.layout.activity_aluno) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val txvNomeAluno = findViewById<TextView>(R.id.txv_nome_aluno)
        val txvTurmaAluno = findViewById<TextView>(R.id.txv_turma_aluno)
        val fabVoltaTelaCadastro =
            findViewById<FloatingActionButton>(R.id.fab_volta_tela_cadastro)

        val btnGerarMatricula =
            findViewById<Button>(R.id.btn_gerar_matricula)

        val txvMatriculaAluno =
            findViewById<TextView>(R.id.txv_matricula_aluno)

        val dao = AlunoDao()
        val aluno = dao.buscar()

        txvNomeAluno.text = "Aluno: ${aluno?.nome}"
        txvTurmaAluno.text = "Turma: ${aluno?.turma}"

        fabVoltaTelaCadastro.setOnClickListener {
            finish()
        }

        btnGerarMatricula.setOnClickListener {
            val matricula = dao.gerarMatricula()

            txvMatriculaAluno.text = "Matrícula: $matricula"
        }
    }
}