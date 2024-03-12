package com.example.parkingsystem.models

import com.google.gson.annotations.SerializedName

data class pontos(
    @SerializedName("nome")
    var nome : String,
    @SerializedName("latitude")
    var preco : Double,
    @SerializedName("longitude")
    var longitude : Double,
    @SerializedName("latitude")
    var latitude : Double,
)
