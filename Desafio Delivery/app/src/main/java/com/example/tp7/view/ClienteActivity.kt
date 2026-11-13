package com.example.tp7.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.round

class ClienteActivity : AppCompatActivity() {
    private val itens = mutableListOf<Item>()
    private val idsSelecionados = mutableSetOf<String>()
    private var restauranteSelecionado: String? = null
    private lateinit var adapter: ItemAdapter
    private lateinit var txtTotal: TextView
    private lateinit var btnPedido: Button
    private var salvandoPedido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            abrirLogin()
            return
        }
        setContentView(R.layout.activity_cliente)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCardapio)
        txtTotal = findViewById(R.id.txtTotalPedido)
        btnPedido = findViewById(R.id.btnFazerPedido)
        adapter = ItemAdapter(itens, idsSelecionados, ::alterarSelecao)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnPedido.setOnClickListener { fazerPedido() }
        findViewById<Button>(R.id.btnSairCliente).setOnClickListener { sair() }
        mostrarTotal()
        carregarCardapio()
    }

    private fun carregarCardapio() {
        FirebaseFirestore.getInstance().collection("itens").get()
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
                findViewById<TextView>(R.id.txtEstadoCardapio).text =
                    if (itens.isEmpty()) "Nenhum item disponível no momento." else "Marque os itens do mesmo restaurante."
            }
            .addOnFailureListener { erro ->
                if (isFinishing || isDestroyed) return@addOnFailureListener
                findViewById<TextView>(R.id.txtEstadoCardapio).text = "Não foi possível carregar o cardápio."
                AlertDialog.Builder(this)
                    .setTitle("Não foi possível carregar o cardápio")
                    .setMessage(erro.localizedMessage)
                    .setPositiveButton("Tentar novamente") { _, _ -> carregarCardapio() }
                    .setNegativeButton("Fechar", null)
                    .show()
            }
    }

    private fun alterarSelecao(item: Item, marcado: Boolean): Boolean {
        if (marcado) {
            val restauranteAtual = restauranteSelecionado
            if (restauranteAtual != null && restauranteAtual != item.restauranteId) {
                Toast.makeText(this, "Cada pedido pode ter itens de apenas um restaurante.", Toast.LENGTH_SHORT).show()
                return false
            }
            restauranteSelecionado = item.restauranteId
            idsSelecionados.add(item.id)
        } else {
            idsSelecionados.remove(item.id)
            if (idsSelecionados.isEmpty()) restauranteSelecionado = null
        }
        mostrarTotal()
        return true
    }

    private fun mostrarTotal() {
        val total = itens.filter { idsSelecionados.contains(it.id) }.sumOf { it.preco }
        txtTotal.text = "Total: ${moeda(round(total * 100) / 100)}"
        btnPedido.isEnabled = idsSelecionados.isNotEmpty() && !salvandoPedido
    }

    private fun fazerPedido() {
        if (salvandoPedido) return
        val cliente = FirebaseAuth.getInstance().currentUser ?: run {
            abrirLogin()
            return
        }
        val selecionados = itens.filter { idsSelecionados.contains(it.id) }
        val restauranteIds = selecionados.map { it.restauranteId }.distinct()
        if (selecionados.isEmpty()) {
            Toast.makeText(this, "Escolha ao menos um item.", Toast.LENGTH_SHORT).show()
            return
        }
        if (restauranteIds.size != 1) {
            Toast.makeText(this, "Escolha itens de apenas um restaurante.", Toast.LENGTH_SHORT).show()
            return
        }

        val total = round(selecionados.sumOf { it.preco } * 100) / 100
        val itensPedido = selecionados.map { item ->
            hashMapOf(
                "nome" to item.nome,
                "preco" to item.preco,
                "restauranteId" to item.restauranteId,
                "restauranteNome" to item.restauranteNome
            )
        }
        val nomeCliente = cliente.displayName?.takeIf { it.isNotBlank() } ?: cliente.email.orEmpty()
        val pedido = hashMapOf<String, Any>(
            "clienteId" to cliente.uid,
            "clienteNome" to nomeCliente,
            "clienteEmail" to cliente.email.orEmpty(),
            "itens" to itensPedido,
            "total" to total,
            "criadoEm" to FieldValue.serverTimestamp()
        )

        salvandoPedido = true
        btnPedido.text = "Salvando pedido..."
        mostrarTotal()
        FirebaseFirestore.getInstance().collection("pedidos").add(pedido)
            .addOnSuccessListener {
                if (isFinishing || isDestroyed) return@addOnSuccessListener
                idsSelecionados.clear()
                restauranteSelecionado = null
                adapter.notifyDataSetChanged()
                salvandoPedido = false
                btnPedido.text = "Fazer pedido"
                mostrarTotal()
                Toast.makeText(this, "Pedido criado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { erro ->
                salvandoPedido = false
                btnPedido.text = "Fazer pedido"
                mostrarTotal()
                Toast.makeText(this, "Não foi possível criar o pedido: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
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

    private fun moeda(valor: Double): String = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}
