package com.soportec.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlApi
import com.soportec.aquitoyapp.modelos.Cliente
import com.soportec.aquitoyapp.modelos.eventRecyclerView
import com.soportec.aquitoyapp.modelos.rowAdapterClientes
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class GetClienteActivity : AppCompatActivity(), eventRecyclerView {

    var clientes: ArrayList<Cliente>? = null
    var clientesAux: ArrayList<Cliente>? = null
    var datosUsuario: JSONObject? = null
    var datosDomicilio: JSONObject? = null
    var controlapi: ControlApi? = null
    var listaClientes: RecyclerView? = null
    var searchv: SearchView? = null
    var adapter: rowAdapterClientes? = null
    var opcionVista: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_cliente)

        initView()


        //eventos
        //evento de busqueda, cada que el texto cambia en la barra de busqueda se
        //filtran los registros de los clientes

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

    fun initView() {
        controlapi = ControlApi(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        clientes = ArrayList<Cliente>()
        clientesAux = ArrayList<Cliente>()
        opcionVista = intent.getStringExtra("opcionVista")!!.toInt()
        searchv = findViewById<SearchView>(R.id.schGetClUno)
        listaClientes = findViewById(R.id.listGetClUno)
        listaClientes?.layoutManager = LinearLayoutManager(this)
        controlapi!!.getClientes(
            datosUsuario!!.getString("usu_documento"),
            datosUsuario!!.getString("usu_pass"),
            ::cargarClientes
        )
    }

    // funcion que se activa cuando se envia una la peticion de los clientes al servidor
    fun cargarClientes(clientes: JSONObject) {
        clientes.keys().forEach {
            var cliente = clientes.getJSONObject(it)
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

    override fun onCLick(posicion: Int) {

        var vista = Intent(this, NuevoDomicilioActivity::class.java)
        if (opcionVista == 1) {
            datosDomicilio!!.put("id_cliente", clientesAux!!.get(posicion).id_cliente)
            datosDomicilio!!.put("nombre_cliente", clientesAux!!.get(posicion).nombrecomercial)
        }
        if (opcionVista == 2) {
            datosDomicilio!!.put("origen", clientesAux!!.get(posicion).direccion)
        }
        if (opcionVista == 3) {
            datosDomicilio!!.put("destino", clientesAux!!.get(posicion).direccion)
        }

        vista.putExtra("datos_usuario", datosUsuario!!.toString())
        vista.putExtra("datos_domicilio", datosDomicilio!!.toString())
        startActivity(vista)
        finish()

    }


}