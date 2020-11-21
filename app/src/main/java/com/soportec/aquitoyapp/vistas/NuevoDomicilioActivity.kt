package com.soportec.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlApi
import com.soportec.aquitoyapp.controles.ControlSql
import org.json.JSONObject

class NuevoDomicilioActivity : AppCompatActivity() {

    var controlapi: ControlApi? = null
    var controldblite: ControlSql? = null
    var datosUsuario: JSONObject? = null
    var datosDomicilios: JSONObject? = null

    val OPCION_CLIENTE_ESCOGIDO: Int = 1
    val OPCION_CLIENTE_ORIGEN: Int = 2
    val OPCION_CLIENTE_DESTINO: Int = 3


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
            vista.putExtra("opcionVista", OPCION_CLIENTE_ESCOGIDO.toString())
            finish()
            startActivity(vista)
        }
        // boton direccion origen
        findViewById<Button>(R.id.btnNuDoOrigen).setOnClickListener {
            var vista = Intent(this, GetClienteActivity::class.java)
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            vista.putExtra("datos_domicilio", getDatosDomicilio().toString())
            vista.putExtra("opcionVista", OPCION_CLIENTE_ORIGEN.toString())
            finish()
            startActivity(vista)
        }

        //boton direccion destino

        findViewById<Button>(R.id.btnNuDoDestino).setOnClickListener {
            var vista = Intent(this, GetClienteActivity::class.java)
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            vista.putExtra("datos_domicilio", getDatosDomicilio().toString())
            vista.putExtra("opcionVista", OPCION_CLIENTE_DESTINO.toString())
            finish()
            startActivity(vista)
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

    //recoge todos los datos del formulario y los retorna
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

    //intriduce todos los datos o compos al formulario
    //los datos viejedel objeto json datosDomicilio que se tranmite entre vistas
    fun setDatosDomicilio() {
        findViewById<TextView>(R.id.txtNuDoTres).text =
            datosDomicilios!!.getString("nombre_cliente")
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