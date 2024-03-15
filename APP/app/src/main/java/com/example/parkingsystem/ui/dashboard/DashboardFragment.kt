package com.example.parkingsystem.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentDashboardBinding
import com.example.parkingsystem.controllers.APIControllers.PontosService
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.models.pontos
import com.example.parkingsystem.ui.home.HomeFragment
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var submitButton : Button
    private lateinit var nomeInput : TextInputEditText
    private lateinit var precoInput : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize your views here
        submitButton = root.findViewById(R.id.submitButton)
        nomeInput = root.findViewById(R.id.nomeInput)
        precoInput = root.findViewById(R.id.precoInput)

        // Set click listener after initializing the submitButton
        submitButton.setOnClickListener {
            val nomeEstacionamento = nomeInput.text.toString()
            val precoEstacionamento = precoInput.text.toString()

            val ponto = pontos(nomeEstacionamento, precoEstacionamento.toDouble(), HomeFragment.longitude, HomeFragment.latitude)

            // Obtenha uma instância do serviço de pontos
            val pontosService = getRetrofitInstance("http://192.168.1.113:8000/").create(PontosService::class.java)

            // Chame o método addPoint na instância do serviço de pontos
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
