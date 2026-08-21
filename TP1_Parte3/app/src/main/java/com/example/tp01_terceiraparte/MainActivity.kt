package com.example.tp01_terceiraparte

import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val edtTensao = findViewById<EditText>(R.id.edt_tensao)
        val edtCorrente = findViewById<EditText>(R.id.edt_corrente)
        val edtResistencia = findViewById<EditText>(R.id.edt_resistencia)
        val btnCalcular = findViewById<Button>(R.id.btn_calcular)
        val txvExibir = findViewById<TextView>(R.id.txv_exibir)

        btnCalcular.setOnClickListener {
            val tensao = edtTensao.text.toString()
            val corrente = edtCorrente.text.toString()
            val resistencia = edtResistencia.text.toString()

            if (tensao.isEmpty()){
                val correnteValor = corrente.toDouble()
                val resistenciaValor = resistencia.toDouble()
                val tensaoValor = correnteValor * resistenciaValor

                txvExibir.setText("O valor da tensão é $tensaoValor")
            }

            else if (corrente.isEmpty()){
                val resistenciaValor = resistencia.toDouble()
                val tensaoValor = tensao.toDouble()
                val correnteValor = tensaoValor / resistenciaValor

                txvExibir.setText("O valor da corrente é $correnteValor")
            }

            else if (resistencia.isEmpty()){
                val tensaoValor = tensao.toDouble()
                val correnteValor = corrente.toDouble()
                val resistenciaValor = tensaoValor / correnteValor

                txvExibir.setText("O valor da resistência é $resistenciaValor")
            }

            else {
                txvExibir.setText("Todos os campos foram preenchidos. Por favor, tente novamente!")
            }

        }
    }
}