package com.example.parkingsystem.controllers.APIControllers

import com.example.parkingsystem.models.DistanciaResponse
import com.example.parkingsystem.models.pontos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PontosService {
    @GET("/GetEstacionamentos/")
    fun getPoints(): Call<List<pontos>>

    @POST("/AddEstacionamento/")
    fun addPoint(@Body ponto: pontos): Call<Void>
    @GET("/CalcularDistancia/")
    fun CalcularDistancia(
        @Query("lat1") lat1: Double,
        @Query("lon1") lon1: Double,
        @Query("lat2") lat2: Double,
        @Query("lon2") lon2: Double
    ): Call<DistanciaResponse>
}
