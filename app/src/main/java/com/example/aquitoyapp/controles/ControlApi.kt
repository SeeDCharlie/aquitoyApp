package com.example.aquitoyapp.controles

import android.content.Context
import com.example.aquitoyapp.modelos.Api
import org.json.JSONObject
import java.io.Serializable

class ControlApi(context: Context) : Serializable {

    private val api = Api(context)

    fun loggin(username: String, passw: String, funcion: (datos: JSONObject) -> Unit) {
        val datos = JSONObject()
        datos.put("loggin", true)
        datos.put("username", username)
        datos.put("password", passw)
        this.api.respuestaPost(datos, "log_api.php", funcion)
    }

    fun logout(documento: String, contraseña: String, funcion: (datos: JSONObject) -> Unit) {
        val datos = JSONObject()
        datos.put("logout", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        this.api.respuestaPost(datos, "logOut.php", funcion)
    }

    fun domicilios_disponibles(
        id_user: Int,
        documento: String,
        contraseña: String,
        funcion: (datos: JSONObject) -> Unit
    ) {
        val datos = JSONObject()
        datos.put("domicilios_disponibles", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        datos.put("id_user", id_user)
        this.api.respuestaPost(datos, "domiciliosDisponibles.php", funcion)
    }


}