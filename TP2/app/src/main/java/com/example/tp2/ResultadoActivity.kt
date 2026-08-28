package com.example.tp2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ResultadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_resultado)

        // TextViews
        val txvLivro = findViewById<TextView>(R.id.txv_livro)
        val txvAutor = findViewById<TextView>(R.id.txv_autor)

        // FloatingActionButton
        val fabVolta = findViewById<FloatingActionButton>(R.id.fabVolta)

        // Recebe os dados enviados pela MainActivity
        val livro = intent.getStringExtra("livro")
        val autor = intent.getStringExtra("autor")

        // Exibe os dados na tela
        txvLivro.text = "Livro: $livro"
        txvAutor.text = "Autor: $autor"

        // Volta para a tela anterior
        fabVolta.setOnClickListener {
            finish()
        }
    }
}