package com.soportec.aquitoyapp.controles

import android.content.Context
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.DomDisponible
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.modelos.rowAdapterDomDisp
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import org.json.JSONObject

class ControlDomiciliosDisponibles(var context: Context?, var fragment: Fragment) : apiInterfaz {


    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    var datosUsuario: JSONObject? = NavegacionActivity.datosUsuario
    var domicilios_disponibles: JSONObject? = null


    fun cargarDomicilios() {
        val datos = JSONObject()
        datos.put("domicilios_disponibles", true)
        datos.put("documento", datosUsuario?.getString("usu_documento"))
        datos.put("contraseña", datosUsuario?.getString("usu_pass"))
        datos.put("id_user", datosUsuario?.getString("usu_id"))
        respuestaPost(datos, "domiciliosDisponibles.php")
    }


    //manejo de respuestas correctas del servidor
    override fun acionPots(obj: JSONObject) {
        //tomamoslos domicilios disponibles que vienen en 'obj' (objeto json)
        //y los guaedamos en una listView
        var listViewDomicilios = fragment.view?.findViewById<ListView>(R.id.listViewUno)
        var listaDatosDom = mutableListOf<DomDisponible>()
        domicilios_disponibles = obj
        obj.keys().forEach {
            var dom: JSONObject = obj.getJSONObject(it)
            listaDatosDom.add(
                DomDisponible(
                    dom.getString("cli_nombre"),
                    dom.getString("dom_origen"),
                    dom.getString("dom_destino"),
                    dom.getString("estadodom_nombre")
                )
            )
        }
        listViewDomicilios?.adapter = rowAdapterDomDisp(context!!, R.layout.row_uno, listaDatosDom)
    }
    //manejo de errores de las peticiones al servidor
    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
    }
}