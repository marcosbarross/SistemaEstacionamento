package com.example.sistemaestacionamentotcc.misc

import android.content.Context
import android.util.Log
import android.view.MenuItem
import com.example.sistemaestacionamentotcc.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions

class TypeAndStyle {
    fun setMapStyle(googleMap: GoogleMap, context: Context){
        try {
            val sucess = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.stylemap
                )
            )
            if (!sucess) {
                Log.d("Maps", "Erro ao estilizar o mapa.")
            }
        }catch (e: Exception){
            Log.d("Maps", "Erro ao estilizar o mapa.")
        }
    }

    fun setMapType(item: MenuItem, map: GoogleMap): Boolean {
        when(item.itemId){
            R.id.normal_map -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            R.id.satellite_map -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
        return true
    }
}