package com.example.parkingsystem.controllers.listaControllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingsystem.models.pontos
import com.example.parkingsystem.R

class EstacionamentoAdapter(private val estacionamentos: List<pontos>) :
    RecyclerView.Adapter<EstacionamentoAdapter.EstacionamentoViewHolder>() {

    inner class EstacionamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        val tipoVagaTextView: TextView = itemView.findViewById(R.id.tipoVagaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstacionamentoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estacionamento, parent, false)
        return EstacionamentoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EstacionamentoViewHolder, position: Int) {
        val estacionamento = estacionamentos[position]
        holder.nomeTextView.text = estacionamento.nome
        holder.tipoVagaTextView.text = estacionamento.tipo_vaga.joinToString(", ")
    }

    override fun getItemCount(): Int {
        return estacionamentos.size
    }
}