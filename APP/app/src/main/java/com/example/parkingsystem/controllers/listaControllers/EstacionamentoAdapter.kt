package com.example.parkingsystem.controllers.listaControllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingsystem.models.pontos
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.APIControllers.pontosController
import com.example.parkingsystem.controllers.locationController.LocationController
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EstacionamentoAdapter(
    private val estacionamentos: List<pontos>,
    private val locationController: LocationController
) :
    RecyclerView.Adapter<EstacionamentoAdapter.EstacionamentoViewHolder>() {

    inner class EstacionamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        val infoTextView: TextView = itemView.findViewById(R.id.infoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstacionamentoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estacionamento, parent, false)
        return EstacionamentoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EstacionamentoViewHolder, position: Int) {
        val estacionamento = estacionamentos[position]
        CoroutineScope(Dispatchers.IO).launch {
            val latitude = withContext(Dispatchers.Main) {
                locationController.getLatitude()
            }
            val longitude = withContext(Dispatchers.Main) {
                locationController.getLongitude()
            }
            val distancia = pontosController.getDistancia(estacionamento.latitude, estacionamento.longitude, latitude, longitude)
            withContext(Dispatchers.Main) {
                holder.nomeTextView.text = estacionamento.nome
                holder.infoTextView.text = "${distancia} km, R$ ${estacionamento.preco.toString()}"
            }
        }
    }



    override fun getItemCount(): Int {
        return estacionamentos.size
    }
}