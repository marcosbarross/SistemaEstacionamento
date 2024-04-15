package com.example.parkingsystem.views.usuario

import UsuariosService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.APIControllers.apiUtils
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.models.AuthResponse
import com.example.parkingsystem.models.usuarioAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var loginButton : Button
    private lateinit var usuariosService : UsuariosService
    private lateinit var inscreverLabel : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginButton = view.findViewById(R.id.LoginButton)
        loginButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailInput).text.toString()
            val senha = view.findViewById<EditText>(R.id.passwordInput).text.toString()
            val usuario = usuarioAuth(email, senha)

            usuariosService = apiUtils.getRetrofitInstance(getPathString()).create(UsuariosService::class.java)
            usuariosService.autenticarUsuario(usuario).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        if (authResponse != null) {
                            val idUsuario = authResponse.userId
                            IdLogin.idUsuario = idUsuario
                            Toast.makeText(context, "Autenticação bem-sucedida", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_to_mapa)
                        } else {
                            Toast.makeText(context, "Resposta inválida do servidor", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(context, "Erro ao se comunicar com o servidor", Toast.LENGTH_SHORT).show()
                }
            })
        }

        inscreverLabel = view.findViewById(R.id.inscreverLabel)
        inscreverLabel.setOnClickListener {
            findNavController().navigate(R.id.navigation_cadastro)
        }

        return view
    }

    companion object IdLogin {
       var idUsuario : Int = Int.MIN_VALUE
    }
}