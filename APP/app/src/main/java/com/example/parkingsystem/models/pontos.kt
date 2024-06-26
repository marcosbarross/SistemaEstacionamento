package com.example.parkingsystem.models

import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName

data class pontos(
    @SerializedName("nome")
    var nome : String,
    @SerializedName("tipo_vaga")
    var tipo_vaga : List<String>,
    @SerializedName("horario_abertura")
    var horario_abertura : String,
    @SerializedName("horario_fechamento")
    var horario_fechamento : String,
    @SerializedName("dias_funcionamento")
    var dias_funcionamento : List<String>,
    @SerializedName("longitude")
    var longitude : Double,
    @SerializedName("latitude")
    var latitude : Double,
    @SerializedName("preco")
    var preco : Double,
    @SerializedName("id_usuario")
    var id_usuario : Int,
)
