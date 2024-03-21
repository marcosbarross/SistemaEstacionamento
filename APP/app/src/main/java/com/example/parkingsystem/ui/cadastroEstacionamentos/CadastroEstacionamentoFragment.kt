package com.example.parkingsystem.ui.cadastroEstacionamentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentCadastroEstacionamentosBinding
import com.example.parkingsystem.controllers.APIControllers.PontosService
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.models.pontos
import com.example.parkingsystem.ui.mapa.MapaFragment
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroEstacionamentoFragment : Fragment() {

    private var _binding: FragmentCadastroEstacionamentosBinding? = null
    private val binding get() = _binding!!
    private lateinit var submitButton : Button
    private lateinit var nomeInput : TextInputEditText
    private lateinit var precoInput : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroEstacionamentosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        submitButton = root.findViewById(R.id.submitButton)
        nomeInput = root.findViewById(R.id.nomeInput)
        precoInput = root.findViewById(R.id.precoInput)

        submitButton.setOnClickListener {
            val nomeEstacionamento = nomeInput.text.toString()
            val precoEstacionamento = precoInput.text.toString()
            val ponto = pontos(nomeEstacionamento, precoEstacionamento.toDouble(), MapaFragment.longitude, MapaFragment.latitude)
            val pontosService = getRetrofitInstance(getPathString()).create(PontosService::class.java)

            val call = pontosService.addPoint(ponto)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Estacionamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Falha ao cadastrar estacionamento. Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erro de conexão ao cadastrar estacionamento. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
