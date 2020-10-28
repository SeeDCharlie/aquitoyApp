package com.example.aquitoyapp.vistas

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.MainActivity
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import com.example.aquitoyapp.modelos.DomDisponible
import com.example.aquitoyapp.modelos.rowAdapterDomDisp
import org.json.JSONObject

class DomiciliosDisponiblesActivity : AppCompatActivity() {

    var datosDomicilio: JSONObject? = null
    var controlapi: ControlApi? = null
    var controldblite: ControlSql? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domicilios_disponibles)

        initView()

        //Creacion del evento del boton cerrar sesion
        findViewById<ImageButton>(R.id.btnSiete).setOnClickListener {
            controlapi!!.logout(
                datosDomicilio!!.getString("usu_documento"),
                datosDomicilio!!.getString("usu_pass"),
                ::btnLogOutAction
            )
        }
        //creacion evento lista de domicilios
        findViewById<ListView>(R.id.listViewUno).setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val vista = Intent(this, TomarDomicilioActivity::class.java)
            vista.putExtra("datosDomi", datosDomicilio!!.getString(position.toString()))
            startActivity(vista)
        }

    }

    private fun btnLogOutAction(datos: JSONObject?) {
        val valores = ContentValues().apply { put("activo", 0) }
        controldblite!!.actualizarDato(
            "sesiones",
            valores,
            "documento = ?",
            arrayOf(datosDomicilio!!.getString("usu_documento"))
        )
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun initView() {
        controlapi = ControlApi(this)
        controldblite = ControlSql(this)
        datosDomicilio = JSONObject(intent.getStringExtra("datos_usuario"))
        controlapi!!.domicilios_disponibles(
            datosDomicilio!!.getInt("usu_id"),
            datosDomicilio!!.getString("usu_documento"),
            datosDomicilio!!.getString("usu_pass"),
            ::cargarDomicilios
        )
    }

    fun cargarDomicilios(domicilios: JSONObject) {
        var listViewDomicilios = findViewById<ListView>(R.id.listViewUno)
        var listaDatosDom = mutableListOf<DomDisponible>()

        domicilios.keys().forEach {
            var dom: JSONObject = domicilios.getJSONObject(it)
            listaDatosDom.add(
                DomDisponible(
                    dom.getString("dom_origen"),
                    dom.getString("dom_destino"),
                    dom.getString("estadodom_nombre")
                )
            )

        }
        listViewDomicilios.adapter = rowAdapterDomDisp(this, R.layout.row_uno, listaDatosDom)

    }


}