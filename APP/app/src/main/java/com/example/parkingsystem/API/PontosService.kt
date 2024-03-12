package com.example.parkingsystem.API

import com.example.parkingsystem.models.pontos
import retrofit2.Call
import retrofit2.http.GET

interface PontosService {
    @GET("GetEstacionamentos/")
    fun getPoints(): Call<List<pontos>>
}