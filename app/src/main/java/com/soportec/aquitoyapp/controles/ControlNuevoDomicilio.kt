package com.soportec.aquitoyapp.controles

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import org.json.JSONObject

class ControlNuevoDomicilio(var context: Context?, var fragment: Fragment): apiInterfaz{

    override var baseUrl: String = VariablesConf.BASE_URL_API
        set(value) {}
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)



    fun registrarDomicilio(){
        var datos_domicilio = JSONObject()
        datos_domicilio.put("origen",NavegacionActivity.modNuevoDom.origen)
        datos_domicilio.put("destino",NavegacionActivity.modNuevoDom.destino)
        datos_domicilio.put("descripcion",NavegacionActivity.modNuevoDom.descripcion)
        datos_domicilio.put("id_cliente",NavegacionActivity.modNuevoDom.id_cliente)
        datos_domicilio.put("notas",NavegacionActivity.modNuevoDom.notas)
        val datos = JSONObject()
        datos.put("nuevo_domicilio", true)
        datos.put("documento", NavegacionActivity.datosUsuario!!.getString("usu_documento"))
        datos.put("id_user", NavegacionActivity.datosUsuario!!.getInt("usu_id"))
        datos.put("contraseña", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
        datos.put("datos_domicilio", datos_domicilio)
        respuestaPost(datos, "nuevoDomicilio.php")
    }

    override fun acionPots(obj: JSONObject) {
        super.acionPots(obj)

        NavegacionActivity.domicilioAux = obj.getJSONObject("datos_domicilio")
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        fragment.findNavController().navigate(R.id.action_nuevoDomicilioFrag_to_tomarDomicilioFrag)

    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Snackbar.make(fragment.requireView(), msj, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
