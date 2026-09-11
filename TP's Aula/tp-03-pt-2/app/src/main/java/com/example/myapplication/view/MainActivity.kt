package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.dao.ImcDao

class MainActivity : AppCompatActivity(R.layout.activity_main) {

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

        val edtPesoUsuario = findViewById<EditText>(R.id.edt_peso_usuario)
        val edtAlturaUsuario = findViewById<EditText>(R.id.edt_altura_usuario)
        val btnCalcular = findViewById<Button>(R.id.btn_calcular)

        val dao = ImcDao()

        btnCalcular.setOnClickListener {

            val peso = edtPesoUsuario.text.toString().toDoubleOrNull()
            val altura = edtAlturaUsuario.text.toString().toDoubleOrNull()

            if (peso == null || altura == null || peso <= 0 || altura <= 0) {

                Toast.makeText(
                    this,
                    "Digite um peso e uma altura válidos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            dao.calcular(peso, altura)

            val intent = Intent(this, ImcActivity::class.java)
            startActivity(intent)
        }
    }
}