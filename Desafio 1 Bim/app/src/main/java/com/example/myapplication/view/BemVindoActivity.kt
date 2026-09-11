package com.example.myapplication.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import kotlin.math.log

class BemVindoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bem_vindo)

        val txvBemVindo = findViewById<TextView>(R.id.txv_bem_vindo)
        val btnVoltar = findViewById<Button>(R.id.btn_voltar)

        val login = intent.getStringExtra("login")
        txvBemVindo.text = "Bem-vindo, ${login}!"

        btnVoltar.setOnClickListener{
            finish()
        }
    }
}