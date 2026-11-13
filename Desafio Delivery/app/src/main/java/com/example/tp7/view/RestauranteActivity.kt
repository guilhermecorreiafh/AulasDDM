package com.example.tp7.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp7.R
import com.example.tp7.adapter.ItemAdapter
import com.example.tp7.model.Item
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class RestauranteActivity : AppCompatActivity() {
    private val itens = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter
    private lateinit var edtNome: EditText
    private lateinit var edtPreco: EditText
    private lateinit var btnCriar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            abrirLogin()
            return
        }
        setContentView(R.layout.activity_restaurante)

        edtNome = findViewById(R.id.edtNomeItem)
        edtPreco = findViewById(R.id.edtPrecoItem)
        btnCriar = findViewById(R.id.btnCriarItem)
        adapter = ItemAdapter(itens)
        findViewById<RecyclerView>(R.id.recyclerMeusItens).apply {
            layoutManager = LinearLayoutManager(this@RestauranteActivity)
            adapter = this@RestauranteActivity.adapter
        }
        btnCriar.setOnClickListener { criarItem() }
        findViewById<Button>(R.id.btnSairRestaurante).setOnClickListener { sair() }
        carregarMeusItens()
    }

    private fun criarItem() {
        val usuario = FirebaseAuth.getInstance().currentUser ?: run {
            abrirLogin()
            return
        }
        val nome = edtNome.text.toString().trim()
        val textoPreco = edtPreco.text.toString().trim()
        if (nome.isEmpty()) {
            edtNome.error = "Informe o nome do item"
            return
        }
        if (!textoPreco.matches(Regex("\\d+(?:[,.]\\d{1,2})?"))) {
            edtPreco.error = "Informe um preço válido com até duas casas decimais"
            return
        }
        val preco = textoPreco.replace(',', '.').toDoubleOrNull()
        if (preco == null || preco <= 0.0) {
            edtPreco.error = "O preço deve ser maior que zero"
            return
        }

        val restauranteNome = usuario.displayName?.takeIf { it.isNotBlank() } ?: usuario.email.orEmpty()
        val dados = hashMapOf(
            "nome" to nome,
            "preco" to preco,
            "restauranteId" to usuario.uid,
            "restauranteNome" to restauranteNome
        )
        btnCriar.isEnabled = false
        btnCriar.text = "Salvando..."
        FirebaseFirestore.getInstance().collection("itens").add(dados)
            .addOnSuccessListener {
                if (isFinishing || isDestroyed) return@addOnSuccessListener
                edtNome.text.clear()
                edtPreco.text.clear()
                Toast.makeText(this, "Item criado com sucesso!", Toast.LENGTH_SHORT).show()
                carregarMeusItens()
                habilitarCriacao()
            }
            .addOnFailureListener { erro ->
                if (isFinishing || isDestroyed) return@addOnFailureListener
                Toast.makeText(this, "Não foi possível criar o item: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
                habilitarCriacao()
            }
    }

    private fun carregarMeusItens() {
        val usuario = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseFirestore.getInstance().collection("itens")
            .whereEqualTo("restauranteId", usuario.uid)
            .get()
            .addOnSuccessListener { resultado ->
                if (isFinishing || isDestroyed) return@addOnSuccessListener
                itens.clear()
                itens.addAll(resultado.documents.mapNotNull { doc ->
                    val nome = doc.getString("nome") ?: return@mapNotNull null
                    val preco = (doc.get("preco") as? Number)?.toDouble() ?: return@mapNotNull null
                    Item(
                        id = doc.id,
                        nome = nome,
                        preco = preco,
                        restauranteId = doc.getString("restauranteId").orEmpty(),
                        restauranteNome = doc.getString("restauranteNome").orEmpty()
                    )
                })
                adapter.notifyDataSetChanged()
                findViewById<TextView>(R.id.txtEstadoMeusItens).text =
                    if (itens.isEmpty()) "Você ainda não cadastrou itens." else "Itens cadastrados: ${itens.size}"
            }
            .addOnFailureListener { erro ->
                if (isFinishing || isDestroyed) return@addOnFailureListener
                findViewById<TextView>(R.id.txtEstadoMeusItens).text = "Não foi possível carregar seus itens."
                AlertDialog.Builder(this)
                    .setTitle("Não foi possível carregar seus itens")
                    .setMessage(erro.localizedMessage)
                    .setPositiveButton("Tentar novamente") { _, _ -> carregarMeusItens() }
                    .setNegativeButton("Fechar", null)
                    .show()
            }
    }

    private fun habilitarCriacao() {
        btnCriar.isEnabled = true
        btnCriar.text = "Criar item"
    }

    private fun sair() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Você saiu da conta", Toast.LENGTH_SHORT).show()
                abrirLogin()
            } else {
                Toast.makeText(this, "Não foi possível sair da conta.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun abrirLogin() {
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}
