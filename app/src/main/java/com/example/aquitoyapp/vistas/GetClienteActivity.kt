package com.example.aquitoyapp.vistas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.modelos.Cliente
import com.example.aquitoyapp.modelos.rowAdapterClientes
import org.json.JSONObject

class GetClienteActivity : AppCompatActivity() {

    var clientes: ArrayList<Cliente>? = null
    var datosUsuario: JSONObject? = null
    var datosDomicilio: JSONObject? = null
    var controlapi: ControlApi? = null
    var listaCli: RecyclerView? = null
    var layoutM: RecyclerView.LayoutManager? = null
    var adaptador: rowAdapterClientes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_cliente)

        initView()
    }

    fun initView() {
        controlapi = ControlApi(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        clientes = ArrayList()
        listaCli = findViewById<RecyclerView>(R.id.listGetClUno)
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
                    cli.getString("cli_telefonos")
                )
            )
        }
        layoutM = LinearLayoutManager(this)
        adaptador = rowAdapterClientes(this.clientes!!)
        listaCli?.layoutManager = layoutM
        listaCli?.adapter = adaptador
    }


}