package com.example.filmesfavoritos.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.filmesfavoritos.databinding.ActivityMainBinding
import com.example.filmesfavoritos.databinding.DialogFilmeBinding
import com.example.filmesfavoritos.model.Filme
import com.example.filmesfavoritos.viewmodel.FilmeViewModel

/**
 * VIEW: só exibe dados e repassa ações do usuário ao ViewModel (via View Binding).
 */
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: FilmeViewModel by viewModels()

    private lateinit var adapter: FilmeAdapter

    // Dialog aberto no momento (para atualizar a prévia da foto escolhida na galeria)
    private var dialogBinding: DialogFilmeBinding? = null

    private val escolherImagem =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                dialogBinding?.let { d ->
                    d.editFoto.setText(it.toString())
                    d.imagePreview.load(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = FilmeAdapter(
            onEditar = { mostrarDialog(it) },
            onRemover = { viewModel.remover(it) }
        )
        binding.recyclerFilmes.layoutManager = LinearLayoutManager(this)
        binding.recyclerFilmes.adapter = adapter

        binding.fabAdicionar.setOnClickListener { mostrarDialog(null) }

        viewModel.filmes.observe(this) { lista ->
            adapter.submitList(lista)
            binding.textVazio.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.erro.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.erroConsumido()
            }
        }
    }

    /** Abre o diálogo para adicionar (filme == null) ou editar um filme. */
    private fun mostrarDialog(filme: Filme?) {
        val d = DialogFilmeBinding.inflate(layoutInflater)
        dialogBinding = d

        if (filme != null) {
            d.editNome.setText(filme.nome)
            d.editNota.setText(filme.nota.toString())
            d.editFoto.setText(filme.foto)
            d.imagePreview.load(filme.foto)
        }

        d.buttonGaleria.setOnClickListener { escolherImagem.launch(arrayOf("image/*")) }
        d.buttonPrevia.setOnClickListener { d.imagePreview.load(d.editFoto.text.toString()) }

        AlertDialog.Builder(this)
            .setTitle(if (filme == null) "Novo filme" else "Editar filme")
            .setView(d.root)
            .setPositiveButton("Salvar") { _, _ ->
                viewModel.salvar(
                    id = filme?.id,
                    nome = d.editNome.text.toString(),
                    notaTexto = d.editNota.text.toString(),
                    foto = d.editFoto.text.toString()
                )
            }
            .setNegativeButton("Cancelar", null)
            .setOnDismissListener { dialogBinding = null }
            .show()
    }
}
