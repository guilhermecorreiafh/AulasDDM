package com.example.myapplication.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.dao.ImcDao

class ImcActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        val txvImc = findViewById<TextView>(R.id.txv_imc)

        val txvClassificacaoImc =
            findViewById<TextView>(R.id.txv_classificacao_imc)

        val btnVoltarCalculo =
            findViewById<Button>(R.id.btn_voltar_calculo)

        val dao = ImcDao()

        val imc = dao.buscar()

        txvImc.text = "IMC: %.2f".format(imc?.valor)

        txvClassificacaoImc.text =
            "Classificação: ${imc?.classificacao}"

        Log.d(
            "IMC",
            "IMC: ${imc?.peso} / (${imc?.altura} * ${imc?.altura}) = ${imc?.valor}"
        )

        Log.d(
            "IMC",
            "Classificação: ${imc?.classificacao}"
        )

        btnVoltarCalculo.setOnClickListener {
            finish()
        }
    }
}