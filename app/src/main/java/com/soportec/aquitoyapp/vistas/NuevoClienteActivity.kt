package com.soportec.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlApi
import org.json.JSONObject

class NuevoClienteActivity : AppCompatActivity() {

    var datosUsuario: JSONObject? = null
    var controlapi: ControlApi? = null
    var datosDomicilio: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        initView()

        //eventos
        //boton registrar
        findViewById<Button>(R.id.btnNuClUno).setOnClickListener {
            controlapi!!.registrarCliente(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"),
                getDatosCliente(),
                ::btnRegistrarClienteAction
            )

        }
        // boton cancelar
        findViewById<Button>(R.id.btnNuClDos).setOnClickListener {
            finish()
        }

    }

    fun initView() {
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        controlapi = ControlApi(this)

    }

    fun btnRegistrarClienteAction(obj: JSONObject) {

        Toast.makeText(this, obj.getString("msj"), Toast.LENGTH_SHORT).show()

        datosDomicilio!!.put("id_cliente", obj.getInt("id_cliente"))
        datosDomicilio!!.put("nombre_cliente", obj.getString("nombre_cliente"))

        var v = Intent(this, NuevoDomicilioActivity::class.java)
        v.putExtra("datos_usuario", datosUsuario!!.toString())
        v.putExtra("datos_domicilio", datosDomicilio!!.toString())

        startActivity(v)

        finish()
    }

    fun getDatosCliente(): JSONObject {
        var dats = JSONObject()
        dats.put("nombre", findViewById<EditText>(R.id.edtNuClUno).text.toString())
        dats.put("correo", findViewById<EditText>(R.id.edtNuClDos).text.toString())
        dats.put("telefono", findViewById<EditText>(R.id.edtNuClTres).text.toString())
        dats.put("direccion", findViewById<EditText>(R.id.edtNuClCuatro).text.toString())
        dats.put("nombreComercial", findViewById<EditText>(R.id.edtNuClCinco).text.toString())
        dats.put("nit", findViewById<EditText>(R.id.edtNuClSeis).text.toString())
        dats.put("web", findViewById<EditText>(R.id.edtNuClSiete).text.toString())
        dats.put("notas", findViewById<EditText>(R.id.edtNuClOcho).text.toString())
        dats.put("contacto", findViewById<EditText>(R.id.edtNuClNueve).text.toString())
        return dats
    }


}