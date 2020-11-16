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
        // boton direccion origen
        findViewById<Button>(R.id.btnNuDoOrigen).setOnClickListener {

        }

        //boton direccion destino

        findViewById<Button>(R.id.btnNuDoOrigen).setOnClickListener {

        }

        //boton registrar domicilio

        findViewById<Button>(R.id.btnNuDoTres).setOnClickListener {
            controlapi!!.registrarDomicilio(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getInt("usu_id"),
                datosUsuario!!.getString("usu_pass"),
                getDatosDomicilio(),
                ::registrarDomicilioAction
            )
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
            datosDomicilios = JSONObject("{'id_cliente':-1}")
            print("sin datos de domicilio")
        }

    }


    fun getDatosDomicilio(): JSONObject {
        var dats = JSONObject()
        dats.put("nombre_cliente", findViewById<TextView>(R.id.txtNuDoTres).text.toString())
        dats.put("origen", findViewById<TextView>(R.id.txtNuDoOrigen).text.toString())
        dats.put("destino", findViewById<TextView>(R.id.txtNuDoDestino).text.toString())
        dats.put("descripcion", findViewById<TextView>(R.id.edtNuDoTres).text.toString())
        dats.put("notas", findViewById<TextView>(R.id.edtNudoCuatro).text.toString())
        dats.put("id_cliente", datosDomicilios!!.getInt("id_cliente"))
        return dats
    }

    fun setDatosDomicilio() {
        findViewById<TextView>(R.id.txtNuDoTres).text =
            "Cliente : " + datosDomicilios!!.getString("nombre_cliente")
        findViewById<TextView>(R.id.txtNuDoOrigen).text = datosDomicilios!!.getString("origen")
        findViewById<TextView>(R.id.txtNuDoDestino).text = datosDomicilios!!.getString("destino")
        findViewById<TextView>(R.id.edtNuDoTres).text = datosDomicilios!!.getString("descripcion")
        findViewById<TextView>(R.id.edtNudoCuatro).text = datosDomicilios!!.getString("notas")
    }

    fun registrarDomicilioAction(obj: JSONObject) {
        var vista = Intent(this, TomarDomicilioActivity::class.java)
        vista.putExtra("datosDomi", obj.getJSONObject("datos_domicilio").toString())
        vista.putExtra("datos_usuario", datosUsuario!!.toString())
        startActivity(vista)
        finish()
    }


}