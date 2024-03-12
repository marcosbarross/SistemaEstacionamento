import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.parkingsystem.API.apiUtils.Companion.getRetrofitInstance
import com.example.parkingsystem.databinding.FragmentHomeBinding
import com.example.parkingsystem.models.pontos
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast
import com.example.parkingsystem.API.PontosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private val floatingActionButton = binding.floatingActionButton

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { googleMap ->
            mMap = googleMap

            mMap.setPadding(0,0,0,200)

            mMap.uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
        }

        floatingActionButton.setOnClickListener {
            val pontosService = getRetrofitInstance("http://127.0.0.1:8000/").create(PontosService::class.java)
            val call = pontosService.getPoints()

            call.enqueue(object : Callback<List<pontos>> {
                override fun onResponse(call: Call<List<pontos>>, response: Response<List<pontos>>) {
                    if (response.isSuccessful) {
                        val pontos = response.body()
                        pontos?.forEach { pontos ->
                            val posicao = LatLng(pontos.latitude, pontos.longitude)
                            mMap.addMarker(MarkerOptions().position(posicao).title(pontos.nome))
                        }
                        Toast.makeText(requireContext(), "Pontos sincronizados", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Falha ao obter os pontos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<pontos>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erro de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
