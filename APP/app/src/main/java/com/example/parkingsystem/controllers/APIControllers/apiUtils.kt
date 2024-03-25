package com.example.parkingsystem.controllers.APIControllers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class apiUtils {
    companion object {
        val path = "http://10.0.0.144:8000/"

        fun getRetrofitInstance(path: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getPathString(): String {
            return path
        }
    }
}
