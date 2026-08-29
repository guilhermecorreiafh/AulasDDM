package com.example.myapplication.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.dao.ImcDao

class ImcActivity : AppCompatActivity(R.layout.activity_imc) {

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

        val txvImc = findViewById<TextView>(R.id.txv_imc)
        val txvClassificacaoImc =
            findViewById<TextView>(R.id.txv_classificacao_imc)

        val dao = ImcDao()

        val imc = dao.buscar()

        txvImc.text = "IMC: %.2f".format(imc?.valor)

        txvClassificacaoImc.text =
            "Classificação: ${imc?.classificacao}"
    }
}