package com.example.parkingsystem.views.mapa

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.PermissionController
import com.example.parkingsystem.controllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.databinding.FragmentMapaBinding
import com.example.parkingsystem.models.pontos
import com.example.parkingsystem.controllers.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.interfaces.PontosService
import com.example.parkingsystem.controllers.pontosController
import com.example.parkingsystem.views.usuario.SharedViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapaFragment : Fragment() {

    private var _binding: FragmentMapaBinding? = null
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var floatingActionButton: FloatingActionButton
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val permissionController by lazy { PermissionController(requireActivity()) }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        floatingActionButton = binding.floatingActionButton
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.setPadding(0, 0, 0, 200)

            permissionController.checkLocationPermission(mMap)

            mMap.uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }
                }

            mMap.setOnMapLongClickListener { latLng ->
                latitude = latLng.latitude
                longitude = latLng.longitude

                sharedViewModel.isLogged.observe(viewLifecycleOwner) { isLogged ->
                    if (isLogged) {
                        findNavController().navigate(R.id.action_home_to_cadastro_estacionamentos)
                    } else {
                        findNavController().navigate(R.id.action_to_login)
                    }
                }
            }
        }

        floatingActionButton.setOnClickListener {
            val pontosService = getRetrofitInstance(getPathString()).create(PontosService::class.java)
            val call = pontosService.getPoints()

            call.enqueue(object : Callback<List<pontos>> {
                override fun onResponse(call: Call<List<pontos>>, response: Response<List<pontos>>) {
                    if (response.isSuccessful) {
                        val pontos = response.body()
                        pontos?.let {
                            if (it.isNotEmpty()) {
                                val builder = LatLngBounds.Builder()
                                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    location?.let {
                                        lifecycleScope.launch {
                                            var quantidadePontos = 0
                                            for (ponto in pontos) {
                                                val posicao = LatLng(ponto.latitude, ponto.longitude)
                                                if (ActivityCompat.checkSelfPermission(
                                                        requireContext(),
                                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                                    ) == PackageManager.PERMISSION_GRANTED
                                                ) {
                                                    val distancia = pontosController.getDistancia(
                                                        ponto.latitude,
                                                        ponto.longitude,
                                                        location.latitude,
                                                        location.longitude
                                                    )
                                                    if (distancia < 10) {
                                                        mMap.addMarker(
                                                            MarkerOptions()
                                                                .position(posicao)
                                                                .title(ponto.nome)
                                                                .snippet("Preço: R$" + ponto.preco.toString())
                                                        )
                                                        quantidadePontos += 1
                                                        builder.include(posicao)
                                                    }
                                                } else {
                                                    permissionController.requestPermission()
                                                }
                                            }
                                            val bounds = builder.build()
                                            val padding = 100
                                            val cameraUpdate = if (quantidadePontos == 1) {
                                                CameraUpdateFactory.newLatLngZoom(bounds.center, 16f)
                                            } else {
                                                CameraUpdateFactory.newLatLngBounds(bounds, padding)
                                            }
                                            mMap.animateCamera(cameraUpdate)
                                            quantidadePontos = 0
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(requireContext(), "Não há estacionamentos próximos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("API_ERROR", "Erro de API: ${response.errorBody()?.string()}")
                        Log.e("MapaFragment", "Falha ao obter os pontos")
                    }
                }

                override fun onFailure(call: Call<List<pontos>>, t: Throwable) {
                    Log.e("NETWORK_ERROR", "Erro de conexão: ${t.message}", t)
                    Log.e("MapaFragment", "Erro de conexão")
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

    companion object {
        var longitude: Double = 0.00
        var latitude: Double = 0.00
    }
}