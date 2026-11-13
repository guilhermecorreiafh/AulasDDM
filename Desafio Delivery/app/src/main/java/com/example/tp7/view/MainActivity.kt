package com.example.tp7.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp7.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtNome = findViewById<TextView>(R.id.txtNome)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val btnSair = findViewById<Button>(R.id.btnSair)

        // Mostra os dados do usuário logado
        val user = FirebaseAuth.getInstance().currentUser
        txtNome.text = "Nome: ${user?.displayName ?: "-"}"
        txtEmail.text = "E-mail: ${user?.email ?: "-"}"

        // Botão Sair: desloga e volta para a tela de login
        btnSair.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    Toast.makeText(this, "Você saiu da conta", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
        }
    }
}
