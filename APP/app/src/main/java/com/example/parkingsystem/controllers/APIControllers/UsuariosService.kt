import com.example.parkingsystem.models.AuthResponse
import com.example.parkingsystem.models.usuarioAuth
import com.example.parkingsystem.models.usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UsuariosService {
    @POST("/AutenticarUsuario/")
    fun autenticarUsuario(@Body usuario: usuarioAuth): Call<AuthResponse>

    @POST("AddUsuario/")
    fun addUsuario(@Body usuario : usuario): Call<Void>
    @GET("/UsuarioPorId/{usuarioId}")
    fun getUsuarioPorId(
        @Path("usuarioId") usuarioId: Int
    ): Call<usuario>
}
