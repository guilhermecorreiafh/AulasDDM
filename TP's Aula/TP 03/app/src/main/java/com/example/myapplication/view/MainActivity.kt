package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.dao.ImcDao

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtPeso = findViewById<EditText>(R.id.edt_peso)
        val edtAltura = findViewById<EditText>(R.id.edt_altura)
        val btnCalcular = findViewById<Button>(R.id.btn_calcular)

        btnCalcular.setOnClickListener {

            val peso = edtPeso.text.toString().toDoubleOrNull()
            val altura = edtAltura.text.toString().toDoubleOrNull()

            if (peso == null || altura == null || altura <= 0) {
                Toast.makeText(
                    this,
                    "Digite peso e altura corretamente",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val dao = ImcDao()

            dao.calcular(peso, altura)

            val intent = Intent(this, ImcActivity::class.java)

            startActivity(intent)
        }
    }
}