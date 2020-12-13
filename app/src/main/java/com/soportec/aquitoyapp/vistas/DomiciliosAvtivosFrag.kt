package com.soportec.aquitoyapp.vistas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.DomDisponible
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.modelos.rowAdapterDomDisp
import org.json.JSONObject

class DomiciliosAvtivosFrag : Fragment(), apiInterfaz {

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = null
    var datosDomicilio : JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_domicilios_avtivos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)


        view.findViewById<ListView>(R.id.listViewDos).setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            NavegacionActivity.domicilioAux = datosDomicilio!!.getJSONObject(position.toString())
            findNavController().navigate(R.id.dom_acts_activo_trans )
            //Toast.makeText(context,"domicilio : ${NavegacionActivity.domicilioAux!!.getString("")}", Toast.LENGTH_SHORT).show()
        }
    }

    fun initView(view:View) {
        requestExecute = Volley.newRequestQueue(context)
        cargarDomicilios()
    }

    fun cargarDomicilios() {
        val datos = JSONObject()
        datos.put("domicilios_activos", true)
        datos.put("documento", NavegacionActivity.datosUsuario?.getString("usu_documento"))
        datos.put("contrasena", NavegacionActivity.datosUsuario?.getString("usu_pass"))
        datos.put("id_user", NavegacionActivity?.datosUsuario?.getString("usu_id"))
        peticionPost(datos, "domiciliosActivos.php")
    }

    override fun acionPost(obj: JSONObject) {
        super.acionPost(obj)
        var listViewDomicilios = view?.findViewById<ListView>(R.id.listViewDos)
        var listaDatosDom = mutableListOf<DomDisponible>()
        datosDomicilio = obj
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
        listViewDomicilios!!.adapter  = rowAdapterDomDisp(view?.context!!, R.layout.row_uno, listaDatosDom)
    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Toast.makeText(context, obj.getString("msj"), Toast.LENGTH_SHORT).show()
    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
    }


}