package com.example.parkingsystem.controllers.APIControllers

import com.example.parkingsystem.models.usuarioAuth
import com.example.parkingsystem.models.usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuariosService {
    @POST("/AutenticarUsuario/")
    fun autenticarUsuario(@Body usuario: usuarioAuth): Call<Void>
    @POST("AddUsuario/")
    fun addUsuario(@Body usuario : usuarios): Call<Void>
}