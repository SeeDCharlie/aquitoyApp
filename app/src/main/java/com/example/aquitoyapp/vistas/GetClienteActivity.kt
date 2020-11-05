package com.example.aquitoyapp.vistas

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.modelos.Cliente
import com.example.aquitoyapp.modelos.rowAdapterClientes
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class GetClienteActivity : AppCompatActivity() {

    var clientes: ArrayList<Cliente>? = null
    var clientesAux: ArrayList<Cliente>? = null
    var datosUsuario: JSONObject? = null
    var datosDomicilio: JSONObject? = null
    var controlapi: ControlApi? = null
    var listaClientes: RecyclerView? = null
    var searchv: SearchView? = null
    var adapter: rowAdapterClientes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_cliente)

        initView()


        //eventos

        searchv!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0!!.isNotEmpty()) {
                    clientesAux!!.clear()
                    clientes!!.forEach {
                        if (it.nombre.toLowerCase(Locale.ROOT)
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
        searchv = findViewById<SearchView>(R.id.schGetClUno)
        listaClientes = findViewById(R.id.listGetClUno)
        listaClientes?.layoutManager = LinearLayoutManager(this)
        controlapi!!.getClientes(
            datosUsuario!!.getString("usu_documento"),
            datosUsuario!!.getString("usu_pass"),
            ::cargarClientes
        )
    }

    fun cargarClientes(clientes: JSONObject) {
        clientes.keys().forEach {
            var cli = clientes.getJSONObject(it)
            this.clientes!!.add(
                Cliente(
                    cli.getInt("cli_id"),
                    cli.getString("cli_nombre"),
                    cli.getString("cli_telefono")
                )
            )
        }
        clientesAux!!.addAll(this.clientes!!)
        adapter = rowAdapterClientes(this.clientesAux!!)
        listaClientes?.adapter = adapter
    }


}