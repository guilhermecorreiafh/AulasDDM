package br.unisanta.apptelas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val edtNomeUsuario = findViewById<EditText>(R.id.edt_nome_usuario)
        val btnSalvar = findViewById<Button>(R.id.btn_salvar)
        val fabAvanca = findViewById<FloatingActionButton>(R.id.fab_avancar)
        var nomeUsuario:String = ""

        btnSalvar.setOnClickListener{
            nomeUsuario = edtNomeUsuario.text.toString()
            edtNomeUsuario.text.toString()
            Toast.makeText(this, "Nome Salvo!", Toast.LENGTH_SHORT).show()
        }

        fabAvanca.setOnClickListener{
            val intent = Intent(this,UsuarioActivity::class.java)
            intent.putExtra("nome_usuario", nomeUsuario)
            startActivity(intent)
        }

    }
}