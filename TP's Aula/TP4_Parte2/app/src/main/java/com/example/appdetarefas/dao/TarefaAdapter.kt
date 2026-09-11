package com.example.appdetarefas.dao

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appdetarefas.R
import com.example.appdetarefas.model.Tarefa

/**
 * "DAO" / Adapter - responsável por gerenciar a lista de tarefas
 * e conectar os dados (Model) com o RecyclerView (View).
 */
class TarefaAdapter(
    private val tarefas: MutableList<Tarefa>
) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>() {

    class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricao)
        val btnConcluir: Button = itemView.findViewById(R.id.btnConcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarefa, parent, false)
        return TarefaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = tarefas[position]

        holder.tvNome.text = tarefa.nome
        holder.tvDescricao.text = tarefa.descricao

        // Aplica/tira o "riscado" conforme o estado da tarefa
        if (tarefa.concluida) {
            holder.tvNome.paintFlags = holder.tvNome.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.btnConcluir.text = "Concluída"
            holder.btnConcluir.isEnabled = false
        } else {
            holder.tvNome.paintFlags = holder.tvNome.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.btnConcluir.text = "Concluir"
            holder.btnConcluir.isEnabled = true
        }

        holder.btnConcluir.setOnClickListener {
            tarefa.concluida = true
            notifyItemChanged(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = tarefas.size

    /** Adiciona uma nova tarefa à lista e atualiza o RecyclerView. */
    fun adicionarTarefa(tarefa: Tarefa) {
        tarefas.add(tarefa)
        notifyItemInserted(tarefas.size - 1)
    }
}
