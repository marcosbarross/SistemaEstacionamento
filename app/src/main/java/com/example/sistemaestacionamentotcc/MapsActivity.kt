package com.example.sistemaestacionamentotcc

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.sistemaestacionamentotcc.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var binding: ActivityMapsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_types_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.normal_map -> {
                map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            R.id.satellite_map -> {
                map!!.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val ifpe = LatLng(-7.945590983202563, -34.85879696004683)
        map!!.addMarker(MarkerOptions().position(ifpe).title("IFPE"))
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(ifpe, 13.5f))

        map!!.uiSettings.apply {
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
        }
        setMapStyle(map!!)
    }

    private fun setMapStyle(googleMap: GoogleMap){
        try {
            val sucess = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.stylemap
                )
            )
            if (!sucess) {
                Log.d("Maps", "Erro ao estilizar o mapa.")
            }
        }catch (e: Exception){
            Log.d("Maps", "Erro ao estilizar o mapa.")
        }
    }

}