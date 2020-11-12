package com.example.aquitoyapp.controles

import android.content.Context
import com.example.aquitoyapp.modelos.Api
import org.json.JSONObject
import java.io.Serializable


class ControlApi(var context: Context) : Serializable {

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

    fun checkSesion(documento: String, contraseña: String, funcion: (datos: JSONObject) -> Unit) {
        val datos = JSONObject()
        datos.put("check_session", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        this.api.respuestaPost(datos, "checkSession.php", funcion)
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

    fun domicilios_activos(
        id_user: Int,
        documento: String,
        contraseña: String,
        funcion: (datos: JSONObject) -> Unit
    ) {
        val datos = JSONObject()
        datos.put("domicilios_activos", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        datos.put("id_user", id_user)
        this.api.respuestaPost(datos, "domiciliosActivos.php", funcion)

    }

    fun empezar_domicilio(
        id_user: Int,
        id_domicilio: Int,
        documento: String,
        contraseña: String,
        funcion: (datos: JSONObject) -> Unit
    ) {
        val datos = JSONObject()
        datos.put("tomar_domicilio", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        datos.put("id_user", id_user)
        datos.put("id_domicilio", id_domicilio)
        this.api.respuestaPost(datos, "empezarDomicilio.php", funcion)

    }

    fun registrarCliente(
        documento: String,
        contraseña: String,
        datos_cliente: JSONObject,
        funcion: (datos: JSONObject) -> Unit
    ) {
        val datos = JSONObject()
        datos.put("nuevo_cliente", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        datos.put("datos_cliente", datos_cliente)
        this.api.respuestaPost(datos, "nuevoCliente.php", funcion)
    }

    fun getClientes(documento: String, contraseña: String, funcion: (datos: JSONObject) -> Unit) {
        val datos = JSONObject()
        datos.put("get_clientes", true)
        datos.put("documento", documento)
        datos.put("contraseña", contraseña)
        this.api.respuestaPost(datos, "getClientes.php", funcion)
    }

    fun registrarDomicilio(
        documento: String,
        id_user: Int,
        contraseña: String,
        datos_domicilio: JSONObject,
        funcion: (datos: JSONObject) -> Unit
    ) {
        val datos = JSONObject()
        datos.put("nuevo_domicilio", true)
        datos.put("documento", documento)
        datos.put("id_user", id_user)
        datos.put("contraseña", contraseña)
        datos.put("datos_domicilio", datos_domicilio)
        this.api.respuestaPost(datos, "nuevoDomicilio.php", funcion)
    }

    fun agregarNotaDomicilio(
        documento: String,
        contraseña: String,
        id_dom: Int,
        nota: String,
        funcion: (datos: JSONObject) -> Unit
    ) {
        val datos = JSONObject()
        datos.put("agregar_nota", true)
        datos.put("documento", documento)
        datos.put("id_dom", id_dom)
        datos.put("contraseña", contraseña)
        datos.put("nota", nota)
        this.api.respuestaPost(datos, "agregarNota.php", funcion)
    }


}