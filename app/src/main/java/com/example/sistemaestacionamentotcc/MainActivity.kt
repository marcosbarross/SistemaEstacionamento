package com.example.sistemaestacionamentotcc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sistemaestacionamentotcc.databinding.ActivityActionBarMainBinding
import com.example.sistemaestacionamentotcc.ui.cadastro.cadastro
import com.example.sistemaestacionamentotcc.ui.lista.lista
import com.example.sistemaestacionamentotcc.ui.mapa.Mapa

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityActionBarMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionBarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //replaceFragment(Mapa())

        binding.navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.maps -> replaceFragment(Mapa())
                R.id.add -> replaceFragment(cadastro())
                R.id.list -> replaceFragment(lista())

                else -> { }

            }
            true
        }
        /**
        setContent {
            SistemaEstacionamentoTCCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mapActivity = Intent(this, MapsActivity::class.java)
                    startActivity(mapActivity)
                }
            }
        } **/
    }
    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}
