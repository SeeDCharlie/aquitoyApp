package com.soportec.aquitoyapp.vistas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class GetClienteFrg : Fragment(), apiInterfaz, eventRecyclerView {

    var clientes: ArrayList<Cliente>? = null
    var clientesAux: ArrayList<Cliente>? = null
    var searchv: SearchView? = null
    var adapter: rowAdapterClientes? = null
    var listaClientes: RecyclerView? = null

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_cliente_frg, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)


        searchv!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0!!.isNotEmpty()) {
                    clientesAux!!.clear()
                    clientes!!.forEach {
                        // busca coincidencias en nombre y nombre comercial en tabla clientes
                        if (it.nombrecomercial.toLowerCase(Locale.ROOT)
                                .contains(p0.toLowerCase(Locale.ROOT)) or it.nombre.toLowerCase(
                                Locale.ROOT
                            )
                                .contains(p0.toLowerCase(Locale.ROOT))
                        ) {
                            clientesAux!!.add(it)
                        }
                    }
                } else {
                    clientesAux!!.clear()
                    clientesAux!!.addAll(clientes!!)
                }
                listaClientes!!.adapter!!.notifyDataSetChanged()
                return true
            }
        })
    }


    fun initView(v:View) {

        //datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        requestExecute = Volley.newRequestQueue(context)
        clientes = ArrayList<Cliente>()
        clientesAux = ArrayList<Cliente>()
        searchv = v.findViewById<SearchView>(R.id.schGetClUno)
        listaClientes = v.findViewById(R.id.listGetClUno)
        listaClientes?.layoutManager = LinearLayoutManager(context)

        val datos = JSONObject()
        datos.put("get_clientes", true)
        datos.put("documento", NavegacionActivity.datosUsuario!!.getString("usu_documento"))
        datos.put("contraseña", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
        respuestaPost(datos, "getClientes.php")

    }

    override fun onCLick(pocicion: Int) {

        try {
            //Toast.makeText(context,"datos domi :: ${NavegacionActivity.modNuevoDom.toString()} " , Toast.LENGTH_SHORT).show()

            if(NavegacionActivity.switchGetCliente == 1){
                NavegacionActivity.modNuevoDom.nombre_cliente = clientes?.get(pocicion)?.nombre
                NavegacionActivity.modNuevoDom.id_cliente = clientes?.get(pocicion)?.id_cliente
            }
            if(NavegacionActivity.switchGetCliente == 2){
                NavegacionActivity.modNuevoDom.origen = clientes?.get(pocicion)?.direccion
            }
            if(NavegacionActivity.switchGetCliente == 3){
                NavegacionActivity.modNuevoDom.destino = clientes?.get(pocicion)?.direccion
            }
            findNavController().navigate(R.id.action_getClienteFrg_to_nuevoDomicilioFrag)
        }catch (ex: Exception){
            Toast.makeText(context,"Error : ${ex} + ${ex.cause}" , Toast.LENGTH_SHORT).show()
        }
    }

    // funcion que se activa cuando se envia la peticion de los clientes al servidor
    override fun acionPots(obj: JSONObject) {
        super.acionPots(obj)
        obj.keys().forEach {
            var cliente = obj.getJSONObject(it)
            this.clientes!!.add(
                Cliente(
                    cliente.getInt("cli_id"),
                    cliente.getString("cli_nombrecomercial"),
                    cliente.getString("cli_nombre"),
                    cliente.getString("cli_telefono"),
                    cliente.getString("cli_direccion")
                )
            )
        }
        clientesAux!!.addAll(this.clientes!!)
        adapter = rowAdapterClientes(this.clientesAux!!, this)
        listaClientes?.adapter = adapter
    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Snackbar.make(requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Snackbar.make(requireView(), msj, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }


}