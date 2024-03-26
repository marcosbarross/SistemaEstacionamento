package com.example.parkingsystem.views.cadastroEstacionamentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentCadastroEstacionamentosBinding
import com.example.parkingsystem.controllers.APIControllers.PontosService
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getPathString
import com.example.parkingsystem.controllers.APIControllers.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.models.pontos
import com.example.parkingsystem.views.login.LoginFragment
import com.example.parkingsystem.views.mapa.MapaFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroEstacionamentoFragment : Fragment() {

    private var _binding : FragmentCadastroEstacionamentosBinding? = null
    private val binding get() = _binding!!
    private lateinit var submitButton : Button
    private lateinit var nomeInput : AppCompatEditText
    private lateinit var precoInput : AppCompatEditText
    private lateinit var checkboxSegunda : CheckBox
    private lateinit var checkboxTerca : CheckBox
    private lateinit var checkboxQuarta : CheckBox
    private lateinit var checkboxQuinta : CheckBox
    private lateinit var checkboxSexta : CheckBox
    private lateinit var checkboxSabado : CheckBox
    private lateinit var checkboxDomingo: CheckBox
    private lateinit var checkboxVaga45 : CheckBox
    private lateinit var checkboxVaga90 : CheckBox
    private lateinit var checkboxVaga180 : CheckBox
    private lateinit var editTextTimeAbertura : AppCompatEditText
    private lateinit var editTextTimeFechamento : AppCompatEditText

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
        checkboxSegunda = root.findViewById(R.id.checkBoxSegunda)
        checkboxTerca = root.findViewById(R.id.checkBoxTerca)
        checkboxQuarta = root.findViewById(R.id.checkBoxQuarta)
        checkboxQuinta = root.findViewById(R.id.checkBoxQuinta)
        checkboxSexta = root.findViewById(R.id.checkBoxSexta)
        checkboxSabado = root.findViewById(R.id.checkBoxSabado)
        checkboxDomingo = root.findViewById(R.id.checkBoxDomingo)
        checkboxVaga45 = root.findViewById(R.id.vaga45)
        checkboxVaga90 = root.findViewById(R.id.vaga90)
        checkboxVaga180 = root.findViewById(R.id.baliza)
        editTextTimeAbertura = root.findViewById(R.id.editTextTime3)
        editTextTimeFechamento = root.findViewById(R.id.editTextTime4)

        submitButton.setOnClickListener {
            val nomeEstacionamento = nomeInput.text.toString()
            val precoEstacionamento = precoInput.text.toString().toDouble()
            val latitude = MapaFragment.latitude
            val longitude = MapaFragment.longitude
            val horarioAbertura = editTextTimeAbertura.text.toString()
            val horarioFechamento = editTextTimeFechamento.text.toString()
            val idUsuario = LoginFragment.idUsuario

            val tipoVaga = mutableListOf<String>()
            if(checkboxVaga180.isChecked) tipoVaga.add("baliza")
            if(checkboxVaga90.isChecked) tipoVaga.add("90")
            if(checkboxVaga45.isChecked) tipoVaga.add("45")

            val diasFuncionamento = mutableListOf<String>()
            if (checkboxSegunda.isChecked) diasFuncionamento.add("Segunda")
            if (checkboxTerca.isChecked) diasFuncionamento.add("Terça")
            if (checkboxQuarta.isChecked) diasFuncionamento.add("Quarta")
            if (checkboxQuinta.isChecked) diasFuncionamento.add("Quinta")
            if (checkboxSexta.isChecked) diasFuncionamento.add("Sexta")
            if (checkboxSabado.isChecked) diasFuncionamento.add("Sábado")
            if (checkboxDomingo.isChecked) diasFuncionamento.add("Domingo")

            val ponto = pontos(nomeEstacionamento, tipoVaga, horarioAbertura, horarioFechamento, diasFuncionamento, longitude, latitude, precoEstacionamento, idUsuario)
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
