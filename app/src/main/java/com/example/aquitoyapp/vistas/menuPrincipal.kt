package com.example.aquitoyapp.vistas

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.MainActivity
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import org.json.JSONObject


class menuPrincipal : AppCompatActivity() {

    var datosUsuario: JSONObject? = null
    var controlapi: ControlApi? = null
    var controldblite: ControlSql? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        initView()

        //Creacion del evento del boton Domicilios activos
        findViewById<ImageButton>(R.id.btnTres).setOnClickListener {
            //inicia la nueva vista y actividad para manejar los domicilios activos o en curso
            val vista = Intent(this, DomiciliosActivosActivity::class.java)
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            startActivity(vista)
        }
        //Creacion del evento del boton Domicilios Disponibles
        findViewById<ImageButton>(R.id.btnDos).setOnClickListener {
            //inicia la nueva vista y actividad para manejar los domicilios disponibles
            val vista = Intent(this, DomiciliosDisponiblesActivity::class.java)
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            startActivity(vista)
        }
        //Creacion del evento del boton cerrar sesion
        findViewById<ImageButton>(R.id.btnSeis).setOnClickListener {
            controlapi!!.logout(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"),
                ::btnLogOutAction
            )
        }
    }

    fun initView() {
        controlapi = ControlApi(this)
        controldblite = ControlSql(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        val txtViewTres = findViewById<TextView>(R.id.textViewTres)
        txtViewTres.text = txtViewTres.text.toString() + datosUsuario!!.getString("usu_nombre")
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
        finish()

    }

}