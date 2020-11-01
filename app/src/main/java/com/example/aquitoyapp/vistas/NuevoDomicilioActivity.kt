package com.example.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import org.json.JSONObject

class NuevoDomicilioActivity : AppCompatActivity() {

    var controlapi: ControlApi? = null
    var controldblite: ControlSql? = null
    var datosUsuario: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_domicilio)


        //creacion de eventos

        //boton nuevo cliente
        findViewById<Button>(R.id.btnNuDoUno).setOnClickListener {
            var vista = Intent(this, NuevoClienteActivity::class.java)
            startActivity(vista)
        }

        //boton cancelar
        findViewById<Button>(R.id.btnNuDoCuatro).setOnClickListener {
            finish()
        }
    }

    fun initView() {
        controlapi = ControlApi(this)
        controldblite = ControlSql(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))

    }


}