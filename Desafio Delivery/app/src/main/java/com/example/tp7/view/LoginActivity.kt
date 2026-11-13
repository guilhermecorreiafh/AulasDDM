package com.example.tp7.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp7.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Recebe o resultado da tela de login do FirebaseUI (igual à documentação)
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se o usuário já está logado, vai direto para a tela principal
        if (FirebaseAuth.getInstance().currentUser != null) {
            abrirTelaPrincipal()
            return
        }

        setContentView(R.layout.activity_login)

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        btnEntrar.setOnClickListener { createSignInIntent() }

        // O FirebaseUI já tem o fluxo de cadastro: ao digitar um e-mail
        // que ainda não existe, ele pede nome e senha e cria a conta.
        btnCadastrar.setOnClickListener { createSignInIntent() }
    }

    private fun createSignInIntent() {
        // Provedor de login: e-mail e senha, permitindo criar contas novas
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            // Login (ou cadastro) feito com sucesso
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this, "Bem-vindo, ${user?.displayName ?: user?.email}!", Toast.LENGTH_SHORT).show()
            abrirTelaPrincipal()
        } else {
            // Se response for null, o usuário apertou "voltar" e cancelou
            if (response == null) {
                Toast.makeText(this, "Login cancelado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro: ${response.error?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
