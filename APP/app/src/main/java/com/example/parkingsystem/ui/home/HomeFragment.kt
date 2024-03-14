package com.example.parkingsystem.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.parkingsystem.API.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.databinding.FragmentHomeBinding
import com.example.parkingsystem.models.pontos
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import android.widget.Toast
import com.example.parkingsystem.API.PontosService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var floatingActionButton : FloatingActionButton

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        floatingActionButton = binding.floatingActionButton

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { googleMap ->
            mMap = googleMap

            mMap.setPadding(0,0,0,200)

            mMap.uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
        }

        floatingActionButton.setOnClickListener {
            val pontosService = getRetrofitInstance("http://192.168.1.113:8000/").create(PontosService::class.java)
            val call = pontosService.getPoints()

            call.enqueue(object : Callback<List<pontos>> {
                override fun onResponse(call: Call<List<pontos>>, response: Response<List<pontos>>) {
                    if (response.isSuccessful) {
                        val pontos = response.body()
                        pontos?.forEach { ponto ->
                            val posicao = LatLng(ponto.latitude, ponto.longitude)
                            mMap.addMarker(MarkerOptions().position(posicao).title(ponto.nome))
                        }
                        val builder = LatLngBounds.Builder()
                        pontos?.forEach { ponto ->
                            val posicao = LatLng(ponto.latitude, ponto.longitude)
                            builder.include(posicao)
                        }
                        val bounds = builder.build()
                        val padding = 100
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

                        mMap.animateCamera(cameraUpdate)

                        Toast.makeText(requireContext(), "Pontos sincronizados", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API_ERROR", "Erro de API: $errorBody")
                        Toast.makeText(requireContext(), "Falha ao obter os pontos", Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onFailure(call: Call<List<pontos>>, t: Throwable) {
                    Log.e("NETWORK_ERROR", "Erro de conexão: ${t.message}", t)
                    Toast.makeText(requireContext(), "Erro de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
