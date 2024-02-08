package com.example.sistemaestacionamentotcc

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.sistemaestacionamentotcc.databinding.ActivityMapsBinding
import com.example.sistemaestacionamentotcc.misc.TypeAndStyle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val typeAndStyle by lazy { TypeAndStyle() }

    private var binding: ActivityMapsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_types_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        typeAndStyle.setMapType(item, map)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val ifpe = LatLng(-7.945590983202563, -34.85879696004683)
        map.addMarker(MarkerOptions().position(ifpe).title("IFPE").draggable(true))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ifpe, 13.5f))

        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
        }
        typeAndStyle.setMapStyle(map, this)

        onMapClicked()
        onMapLogClicked()
    }

    private fun onMapClicked(){
        map.setOnMapClickListener {
            Toast.makeText(this, "Clique simples", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onMapLogClicked(){
        map.setOnMapLongClickListener {
            val marcador = LatLng(it.latitude, it.longitude)
            map.addMarker(MarkerOptions().position(marcador).title("Teste"))
            Toast.makeText(this, "Marcador adicionado!", Toast.LENGTH_SHORT).show()
        }
    }
}