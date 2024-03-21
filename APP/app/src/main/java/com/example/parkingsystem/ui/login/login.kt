package com.example.parkingsystem.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.APIControllers.UsuariosService
import com.example.parkingsystem.controllers.APIControllers.apiUtils
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.models.usuarioAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var loginButton: Button
    private lateinit var usuariosService: UsuariosService

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
            usuariosService.autenticarUsuario(usuario).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Autenticação bem-sucedida", Toast.LENGTH_SHORT).show()
                        // Faça o que precisa ser feito após a autenticação bem-sucedida
                    } else {
                        Toast.makeText(context, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Erro ao se comunicar com o servidor", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return view
    }
}
