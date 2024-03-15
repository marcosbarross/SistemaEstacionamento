package com.example.parkingsystem.controllers.APIControllers

import com.example.parkingsystem.models.pontos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PontosService {
    @GET("GetEstacionamentos/")
    fun getPoints(): Call<List<pontos>>

    @POST("/AddEstacionamento/")
    fun addPoint(@Body ponto: pontos): Call<Void>
}
