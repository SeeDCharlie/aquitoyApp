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
    var listaClientes: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_cliente)

        initView()
    }

    fun initView() {
        controlapi = ControlApi(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        clientes = ArrayList<Cliente>()
        listaClientes = findViewById(R.id.listGetClUno)
        println("antes del de desastre !!!")
        listaClientes?.layoutManager = LinearLayoutManager(this)
        println("despues del de desastre !!!")
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
        println(">>>>>>>><>>< \n Clientes : " + this.clientes!!.toString())

        val adap = rowAdapterClientes(this.clientes!!)
        listaClientes?.adapter = adap
    }


}