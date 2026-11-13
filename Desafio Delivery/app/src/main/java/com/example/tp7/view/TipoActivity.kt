package com.example.tp7.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp7.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TipoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuario = FirebaseAuth.getInstance().currentUser
        if (usuario == null) {
            abrirLogin()
            return
        }

        setContentView(R.layout.activity_tipo)
        val btnCliente = findViewById<Button>(R.id.btnTipoCliente)
        val btnRestaurante = findViewById<Button>(R.id.btnTipoRestaurante)

        btnCliente.setOnClickListener { salvarTipo("Cliente", btnCliente, btnRestaurante) }
        btnRestaurante.setOnClickListener { salvarTipo("Restaurante", btnCliente, btnRestaurante) }
    }

    private fun salvarTipo(tipo: String, btnCliente: Button, btnRestaurante: Button) {
        val usuario = FirebaseAuth.getInstance().currentUser ?: run {
            abrirLogin()
            return
        }
        btnCliente.isEnabled = false
        btnRestaurante.isEnabled = false
        val dados = hashMapOf(
            "nome" to (usuario.displayName?.takeIf { it.isNotBlank() } ?: usuario.email.orEmpty()),
            "email" to usuario.email.orEmpty(),
            "tipo" to tipo
        )

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(usuario.uid)
            .set(dados)
            .addOnSuccessListener {
                val destino = if (tipo == "Cliente") ClienteActivity::class.java else RestauranteActivity::class.java
                startActivity(Intent(this, destino).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }
            .addOnFailureListener { erro ->
                btnCliente.isEnabled = true
                btnRestaurante.isEnabled = true
                Toast.makeText(this, "Não foi possível salvar o perfil: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun abrirLogin() {
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}
