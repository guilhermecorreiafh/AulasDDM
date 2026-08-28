package com.example.tp2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Campos da tela
        val edtLivro = findViewById<EditText>(R.id.edtLivro)
        val edtAutor = findViewById<EditText>(R.id.edtAutor)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)
        val fabAvanca = findViewById<FloatingActionButton>(R.id.fabAvanca)

        // Variáveis para armazenar as informações
        var livro: String = ""
        var autor: String = ""

        // Botão Cadastrar
        btnCadastrar.setOnClickListener {

            livro = edtLivro.text.toString()
            autor = edtAutor.text.toString()

            Toast.makeText(
                this,
                "Livro cadastrado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // FloatingActionButton para avançar
        fabAvanca.setOnClickListener {

            val intent = Intent(this, ResultadoActivity::class.java)

            intent.putExtra("livro", livro)
            intent.putExtra("autor", autor)

            startActivity(intent)
        }
    }
}