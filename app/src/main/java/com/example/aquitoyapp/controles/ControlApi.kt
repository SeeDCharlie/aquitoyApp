package com.example.aquitoyapp.controles

import android.content.Context
import com.example.aquitoyapp.modelos.Api
import org.json.JSONObject

class ControlApi(context: Context) {

    val api = Api(context)

    fun loggin(username: String, passw: String): Boolean {
        val datos = JSONObject()
        datos.put("username", username)
        datos.put("password", passw)

        var respuesta = this.api.respuestaPost(datos, "log_api.php")

        return respuesta.getBoolean("ok")

    }


}