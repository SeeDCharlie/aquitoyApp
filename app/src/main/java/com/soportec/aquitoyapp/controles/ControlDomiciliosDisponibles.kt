package com.soportec.aquitoyapp.controles

import android.content.Context
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.DomDisponible
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.modelos.rowAdapterDomDisp
import org.json.JSONObject

class ControlDomiciliosDisponibles(var context: Context, var fragment: Fragment) : apiInterfaz {


    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    fun cargarDomicilios() {


    }


    override fun acionPots(obj: JSONObject) {
        var listViewDomicilios = fragment.view?.findViewById<ListView>(R.id.listViewUno)
        var listaDatosDom = mutableListOf<DomDisponible>()

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
        listViewDomicilios?.adapter = rowAdapterDomDisp(context, R.layout.row_uno, listaDatosDom)

    }

}