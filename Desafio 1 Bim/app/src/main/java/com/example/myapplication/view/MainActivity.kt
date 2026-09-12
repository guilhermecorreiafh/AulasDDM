package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.dao.LoginDao

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtLogin = findViewById<EditText>(R.id.edt_login)
        val edtSenha = findViewById<EditText>(R.id.edt_senha)
        val btnAvancar = findViewById<Button>(R.id.btn_avancar)

        btnAvancar.setOnClickListener{
            val login = edtLogin.text.toString()
            val senha = edtSenha.text.toString()

            if(login.isEmpty() || senha.isEmpty()){
                Toast.makeText(
                    this,
                    "Preencha login e senha",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if(LoginDao.validar(login, senha)){
                val intent = Intent(this, BemVindoActivity::class.java)
                intent.putExtra("login", login)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Login ou senha inválidos",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}