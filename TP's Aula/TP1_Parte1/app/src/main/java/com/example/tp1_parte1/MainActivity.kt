package com.example.tp1_parte1

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val edtNome = findViewById<EditText>(R.id.edt_nome)
        val edtIdade = findViewById<EditText>(R.id.edt_idade)
        val btnEnviar = findViewById<Button>(R.id.btn_enviar)
        val txvExibir = findViewById<TextView>(R.id.txv_exibir)

        btnEnviar.setOnClickListener {
            val nome = edtNome.text.toString()
            val idade = edtIdade.text.toString().toInt()

            txvExibir.setText("Seja bem-vindo, $nome! Você tem $idade anos.")

            edtNome.text.clear()
            edtIdade.text.clear()
        }

        }
    }