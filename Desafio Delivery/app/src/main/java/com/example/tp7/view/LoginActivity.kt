package com.example.tp7.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp7.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    // Recebe o resultado da tela de login do FirebaseUI (igual à documentação)
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            encaminharUsuario()
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
            encaminharUsuario()
        } else {
            // Se response for null, o usuário apertou "voltar" e cancelou
            if (response == null) {
                Toast.makeText(this, "Login cancelado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro: ${response.error?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun encaminharUsuario() {
        val usuario = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(usuario.uid)
            .get()
            .addOnSuccessListener { documento ->
                if (isFinishing || isDestroyed) return@addOnSuccessListener
                val destino = when (documento.getString("tipo")) {
                    "Cliente" -> ClienteActivity::class.java
                    "Restaurante" -> RestauranteActivity::class.java
                    else -> TipoActivity::class.java
                }
                abrirTela(destino)
            }
            .addOnFailureListener { erro ->
                if (isFinishing || isDestroyed) return@addOnFailureListener
                AlertDialog.Builder(this)
                    .setTitle("Não foi possível carregar seu perfil")
                    .setMessage("Verifique a conexão e tente novamente.\n${erro.localizedMessage.orEmpty()}")
                    .setPositiveButton("Tentar novamente") { _, _ -> encaminharUsuario() }
                    .setNegativeButton("Sair") { _, _ ->
                        AuthUI.getInstance().signOut(this).addOnCompleteListener {
                            if (it.isSuccessful) {
                                abrirTela(LoginActivity::class.java)
                            } else {
                                Toast.makeText(this, "Não foi possível sair da conta.", Toast.LENGTH_LONG).show()
                                encaminharUsuario()
                            }
                        }
                    }
                    .setCancelable(false)
                    .show()
            }
    }

    private fun abrirTela(destino: Class<*>) {
        startActivity(
            Intent(this, destino).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }
}
