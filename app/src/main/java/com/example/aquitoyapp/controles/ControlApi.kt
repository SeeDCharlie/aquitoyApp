package com.example.aquitoyapp.controles

import android.content.Context
import com.example.aquitoyapp.modelos.Api
import org.json.JSONObject

class ControlApi(context: Context) {

    val api = Api(context)

    fun loggin(username: String, passw: String, funcion: (datos: JSONObject) -> Unit) {
        val datos = JSONObject()
        datos.put("loggin", true)
        datos.put("username", username)
        datos.put("password", passw)

        this.api.respuestaPost(datos, "log_api.php", funcion)
    }


}