package com.example.parkingsystem.controllers.APIControllers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class apiUtils {
    companion object {
        val path = "http://192.168.1.116:8000/"

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
