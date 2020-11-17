package com.soportec.aquitoyapp.vistas

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.soportec.aquitoyapp.MainActivity
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlApi
import com.soportec.aquitoyapp.controles.ControlSql
import com.soportec.aquitoyapp.modelos.DomDisponible
import com.soportec.aquitoyapp.modelos.rowAdapterDomDisp
import org.json.JSONObject

class DomiciliosDisponiblesActivity : AppCompatActivity() {

    var datosDomicilio: JSONObject? = null
    var controlapi: ControlApi? = null
    var controldblite: ControlSql? = null
    var datosUsuario: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domicilios_disponibles)

        initView()

        //Creacion del evento del boton cerrar sesion
        findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            controlapi!!.logout(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"),
                ::btnLogOutAction
            )
        }
        //creacion evento lista de domicilios
        findViewById<ListView>(R.id.listViewUno).setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val vista = Intent(this, TomarDomicilioActivity::class.java)
            vista.putExtra("datosDomi", datosDomicilio!!.getString(position.toString()))
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            startActivity(vista)
        }

    }

    fun btnLogOutAction(datos: JSONObject?) {
        val valores = ContentValues().apply { put("activo", 0) }
        controldblite!!.actualizarDato(
            "sesiones",
            valores,
            "documento = ?",
            arrayOf(datosUsuario!!.getString("usu_documento"))
        )
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun initView() {
        controlapi = ControlApi(this)
        controldblite = ControlSql(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        controlapi!!.domicilios_disponibles(
            datosUsuario!!.getInt("usu_id"),
            datosUsuario!!.getString("usu_documento"),
            datosUsuario!!.getString("usu_pass"),
            ::cargarDomicilios
        )
    }

    fun cargarDomicilios(domicilios: JSONObject) {
        var listViewDomicilios = findViewById<ListView>(R.id.listViewUno)
        var listaDatosDom = mutableListOf<DomDisponible>()
        datosDomicilio = domicilios
        domicilios.keys().forEach {
            var dom: JSONObject = domicilios.getJSONObject(it)
            listaDatosDom.add(
                DomDisponible(
                    dom.getString("cli_nombre"),
                    dom.getString("dom_origen"),
                    dom.getString("dom_destino"),
                    dom.getString("estadodom_nombre")
                )
            )

        }
        listViewDomicilios.adapter = rowAdapterDomDisp(this, R.layout.row_uno, listaDatosDom)

    }


}