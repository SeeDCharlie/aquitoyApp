package com.example.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import org.json.JSONObject

class NuevoDomicilioActivity : AppCompatActivity() {

    var controlapi: ControlApi? = null
    var controldblite: ControlSql? = null
    var datosUsuario: JSONObject? = null
    var datosDomicilios: JSONObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_domicilio)

        initView()
        //creacion de eventos

        //boton nuevo cliente
        findViewById<Button>(R.id.btnNuDoUno).setOnClickListener {
            var vista = Intent(this, NuevoClienteActivity::class.java)
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            vista.putExtra("datos_domicilio", getDatosDomicilio().toString())
            finish()
            startActivity(vista)
        }
        //boton cliente existente

        findViewById<Button>(R.id.btnNuDoDos).setOnClickListener {
            var vista = Intent(this, GetClienteActivity::class.java)
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            vista.putExtra("datos_domicilio", getDatosDomicilio().toString())
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
        try {
            datosDomicilios = JSONObject(intent.getStringExtra("datos_domicilio"))
            setDatosDomicilio()
        } catch (error: Exception) {
            print("sin datos de domicilio")
        }

    }


    fun getDatosDomicilio(): JSONObject {
        var dats = JSONObject()
        dats.put("nombre_cliente", findViewById<TextView>(R.id.txtNuDoTres).text.toString())
        dats.put("origen", findViewById<TextView>(R.id.edtNuDoUno).text.toString())
        dats.put("destino", findViewById<TextView>(R.id.edtNuDoDos).text.toString())
        dats.put("descripcion", findViewById<TextView>(R.id.edtNuDoTres).text.toString())
        dats.put("notas", findViewById<TextView>(R.id.edtNudoCuatro).text.toString())
        return dats
    }

    fun setDatosDomicilio() {
        findViewById<TextView>(R.id.txtNuDoTres).text =
            "Cliente : " + datosDomicilios!!.getString("nombre_cliente")
        findViewById<TextView>(R.id.edtNuDoUno).text = datosDomicilios!!.getString("origen")
        findViewById<TextView>(R.id.edtNuDoDos).text = datosDomicilios!!.getString("destino")
        findViewById<TextView>(R.id.edtNuDoTres).text = datosDomicilios!!.getString("descripcion")
        findViewById<TextView>(R.id.edtNudoCuatro).text = datosDomicilios!!.getString("notas")
    }


}