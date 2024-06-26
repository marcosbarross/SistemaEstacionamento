package com.example.parkingsystem.views.usuario

import com.example.parkingsystem.interfaces.UsuariosService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parkingsystem.R
import com.example.parkingsystem.controllers.apiUtils
import com.example.parkingsystem.controllers.apiUtils.Companion.isEmailValid
import com.example.parkingsystem.controllers.apiUtils.Companion.toSHA256
import com.example.parkingsystem.databinding.FragmentCadastroBinding
import com.example.parkingsystem.models.usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroFragment : Fragment() {
    private var _binding : FragmentCadastroBinding? = null
    private val binding get() = _binding!!
    private lateinit var nomeUsuario : AppCompatEditText
    private lateinit var emailUsuario : AppCompatEditText
    private lateinit var senha : AppCompatEditText
    private lateinit var tipoVeiculoCarro : RadioButton
    private lateinit var tipoVeiculoMoto : RadioButton
    private lateinit var botaoCadastro : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        val root: View = binding.root

        nomeUsuario = root.findViewById(R.id.nomeUsuarioInput)
        emailUsuario = root.findViewById(R.id.emailUsuarioInput)
        senha = root.findViewById(R.id.senhaUsuarioInput)
        tipoVeiculoCarro = root.findViewById(R.id.radioButtonCarro)
        tipoVeiculoMoto = root.findViewById(R.id.radioButtonMoto)
        botaoCadastro = root.findViewById(R.id.cadastrarUsuarioButton)
        botaoCadastro.setOnClickListener{

            val nome = nomeUsuario.text.toString()
            val email = emailUsuario.text.toString()
            val senha = senha.text.toString().toSHA256()
            val tipoVeiculo = if (tipoVeiculoCarro.isChecked) "Carro" else "Moto"

            if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()) {
                if (isEmailValid(email)){
                    val novoUsuario = usuario(nome, email, senha, tipoVeiculo)

                    val usuariosService = apiUtils.getRetrofitInstance(apiUtils.getPathString()).create(
                        UsuariosService::class.java)

                    usuariosService.addUsuario(novoUsuario).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                findNavController().navigate(R.id.navigation_login)
                                Toast.makeText(requireContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(requireContext(), "Erro de conexão", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                else{
                    Toast.makeText(requireContext(), "insira um e-mail válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
