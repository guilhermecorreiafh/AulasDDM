package com.example.tp1_segundaparte

import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val edtTemperatura = findViewById<EditText>(R.id.edt_temperatura)
        val btnConverter = findViewById<Button>(R.id.btn_converter)
        val txvExibir = findViewById<TextView>(R.id.txv_exibir)

        btnConverter.setOnClickListener {
            val temperatura = edtTemperatura.text.toString().toDouble()
            val resultado = (temperatura * 1.8) + 32

            txvExibir.setText("A temperatura informada foi $temperatura°C, resultando em $resultado°F.")

        }
        }
    }