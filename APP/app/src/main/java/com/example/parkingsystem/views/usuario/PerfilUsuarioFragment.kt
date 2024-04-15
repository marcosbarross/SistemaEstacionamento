package com.example.parkingsystem.views.usuario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.parkingsystem.R

class PerfilUsuarioFragment : Fragment() {
    private lateinit var sairButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_perfil_usuario, container, false)
        sairButton = rootView.findViewById(R.id.logoutUserButton)

        sairButton.setOnClickListener{
            LoginFragment.IdLogin.idUsuario = Int.MIN_VALUE
            findNavController().navigate(R.id.navigation_login)
        }

        return rootView
    }
}

