package com.example.parkingsystem.views.usuario

import com.example.parkingsystem.interfaces.UsuariosService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.apiUtils
import com.example.parkingsystem.controllers.apiUtils.Companion.toSHA256
import com.example.parkingsystem.models.AuthResponse
import com.example.parkingsystem.models.usuarioAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var loginButton: Button
    private lateinit var usuariosService: UsuariosService
    private lateinit var inscreverLabel: TextView
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (sharedViewModel.isLogged.value == true) {
            findNavController().navigate(R.id.navigation_perfil_usuario)
            return null
        } else {
            val view = inflater.inflate(R.layout.fragment_login, container, false)

            loginButton = view.findViewById(R.id.LoginButton)
            loginButton.setOnClickListener {
                val email = view.findViewById<EditText>(R.id.emailInput).text.toString()
                val senha = view.findViewById<EditText>(R.id.passwordInput).text.toString().toSHA256()
                val usuario = usuarioAuth(email, senha)

                usuariosService = apiUtils.getRetrofitInstance(apiUtils.getPathString()).create(
                    UsuariosService::class.java)
                usuariosService.autenticarUsuario(usuario).enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            val authResponse = response.body()
                            if (authResponse != null) {
                                val idUsuario = authResponse.userId
                                sharedViewModel.setUserId(idUsuario)
                                sharedViewModel.setLogged(true)

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
    }
}
