package com.example.parkingsystem.models

import com.google.gson.annotations.SerializedName

data class usuarios(
    @SerializedName("nome")
    var nome : String,
    @SerializedName("email")
    var email : String,
    @SerializedName("senha")
    var senha : String
)

data class usuarioAuth(
    @SerializedName("email")
    var email : String,
    @SerializedName("senha")
    var senha : String
)
