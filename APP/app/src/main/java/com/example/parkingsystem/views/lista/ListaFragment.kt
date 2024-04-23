package com.example.parkingsystem.views.lista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkingsystem.controllers.APIControllers.PontosService
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.controllers.listaControllers.EstacionamentoAdapter
import com.example.parkingsystem.controllers.locationController.LocationController
import com.example.parkingsystem.databinding.FragmentListaBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingsystem.models.pontosOrdenados
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationController: LocationController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val retrofit = Retrofit.Builder()
            .baseUrl(getPathString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PontosService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            locationController = LocationController(this@ListaFragment, object : LocationController.LocationCallback {
                override fun onLocationReceived(latitude: Double, longitude: Double) {
                }
                override fun onLocationFailed() {
                }
            })

            val latitude = withContext(Dispatchers.Main) {
                locationController.getLatitude()
            }
            val longitude = withContext(Dispatchers.Main) {
                locationController.getLongitude()
            }
            service.getEstacionamentosOrdenados(latitude, longitude).enqueue(object : Callback<List<pontosOrdenados>> {
                override fun onResponse(
                    call: Call<List<pontosOrdenados>>,
                    response: Response<List<pontosOrdenados>>
                ) {
                    if (response.isSuccessful) {
                        val estacionamentos = response.body()
                        estacionamentos?.let {
                            exibirEstacionamentos(estacionamentos)
                        }
                    } else {
                        // TODO: Lidar com erro de resposta da API
                    }
                }
                override fun onFailure(call: Call<List<pontosOrdenados>>, t: Throwable) {
                    Toast.makeText(context, "Falha na requisição: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        return root
    }

    private fun exibirEstacionamentos(estacionamentos: List<pontosOrdenados>) {
        val adapter = EstacionamentoAdapter(estacionamentos, locationController)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}