package com.soportec.aquitoyapp.vistas

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlApi
import org.json.JSONObject

class NuevaNotaDom : AppCompatActivity() {

    var datosDomicilio: JSONObject? = null
    var controlapi: ControlApi? = null
    var datosUsuario: JSONObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_nota_dom)

        initView()

        //eventos

        findViewById<Button>(R.id.btnNuNoUno).setOnClickListener {
            var nota = findViewById<TextView>(R.id.txtNuNoNota).text.toString()
            if (nota != "") {

                controlapi!!.agregarNotaDomicilio(
                    datosUsuario!!.getString("usu_documento"),
                    datosUsuario!!.getString("usu_pass"),
                    datosDomicilio!!.getInt("dom_id"),
                    nota,
                    ::agregarNota
                )
            } else {
                Toast.makeText(this, "El campo 'Nota' esta vacio!", Toast.LENGTH_SHORT).show()
            }

        }
        // boton cancelar
        findViewById<Button>(R.id.btnNuNoDos).setOnClickListener {
            finish()
        }
    }


    fun initView() {
        controlapi = ControlApi(this)
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))

    }

    fun agregarNota(obj: JSONObject) {

        Toast.makeText(this, obj.getString("msj"), Toast.LENGTH_SHORT).show()
        finish()
    }


}