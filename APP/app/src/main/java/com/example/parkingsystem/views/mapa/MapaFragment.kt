package com.example.parkingsystem.views.mapa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.databinding.FragmentMapaBinding
import com.example.parkingsystem.models.pontos
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.location.LocationServices
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.APIControllers.PontosService
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.controllers.permissionsControllers.PermissionController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapaFragment : Fragment() {

    private var _binding: FragmentMapaBinding? = null
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var floatingActionButton : FloatingActionButton
    private lateinit var permissionController: PermissionController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapaBinding.inflate(inflater, container, false)
        permissionController = PermissionController(requireActivity())
        val root: View = binding.root

        floatingActionButton = binding.floatingActionButton

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.setPadding(0,0,0,200)
            
            permissionController.checkLocationPermission(mMap)

            mMap.uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }

            // Move a câmera para a localização atual do dispositivo quando disponível
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }
                }

            mMap.setOnMapLongClickListener{latLng ->
                latitude = latLng.latitude
                longitude = latLng.longitude

                findNavController().navigate(R.id.action_home_to_cadastro_estacionamentos)
            }

        }
        floatingActionButton.setOnClickListener {
            val pontosService = getRetrofitInstance(getPathString()).create(
                PontosService::class.java)
            val call = pontosService.getPoints()

            call.enqueue(object : Callback<List<pontos>> {
                override fun onResponse(call: Call<List<pontos>>, response: Response<List<pontos>>) {
                    if (response.isSuccessful) {
                        val pontos = response.body()
                        pontos?.forEach { ponto ->
                            val posicao = LatLng(ponto.latitude, ponto.longitude)
                            mMap.addMarker(MarkerOptions().position(posicao).title(ponto.nome).snippet(
                                "Preço: R$" + ponto.preco.toString()))
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

    companion object {
        var longitude: Double = 0.00
        var latitude : Double = 0.00
    }
}
