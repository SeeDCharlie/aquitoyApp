package com.soportec.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlApi
import org.json.JSONObject

class TomarDomicilioActivity : AppCompatActivity() {

    var controlapi: ControlApi? = null
    var datosDomicilio: JSONObject? = null
    var datosUsuario: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomar_domicilio)

        initView()

        //Eventos

        //evento del boton 'empezar' para empezar un domicilio
        findViewById<Button>(R.id.btnToDoUno).setOnClickListener {

            controlapi!!.empezar_domicilio(
                datosUsuario!!.getInt("usu_id"),
                datosDomicilio!!.getInt("dom_id"),
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"), ::tomarDomAction
            )

        }
        // evento cancelar
        findViewById<Button>(R.id.btnToDoDos).setOnClickListener {
            finish()
        }
    }

    fun initView() {
        controlapi = ControlApi(this)
        datosDomicilio = JSONObject(intent.getStringExtra("datosDomi"))
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))

        val tvUno = findViewById<TextView>(R.id.tvToDoOrigen)
        val tvDos = findViewById<TextView>(R.id.tvToDoDestino)
        val tvTres = findViewById<TextView>(R.id.tvToDoDescripcion)
        val tvCuatro = findViewById<TextView>(R.id.tvToDoFechaAsig)
        val tvCinco = findViewById<TextView>(R.id.tvToDoCliente)
        val tvSeis = findViewById<TextView>(R.id.tvToDoNotas)

        tvUno.text = tvUno.text.toString() + datosDomicilio!!.getString("dom_origen")
        tvDos.text = tvDos.text.toString() + datosDomicilio!!.getString("dom_destino")
        tvTres.text = tvTres.text.toString() + datosDomicilio!!.getString("dom_descripcion")
        tvCuatro.text = tvCuatro.text.toString() + datosDomicilio!!.getString("dom_fechaasignacion")
        tvCinco.text = tvCinco.text.toString() + datosDomicilio!!.getString("cli_nombre")
        tvSeis.text = tvSeis.text.toString() + datosDomicilio!!.getString("dom_notas")
    }

    fun tomarDomAction(obj: JSONObject) {

        var vista = Intent(this, DomicilioActivoActivity::class.java)
        vista.putExtra("datos_domicilio", datosDomicilio!!.toString())
        vista.putExtra("datos_usuario", datosUsuario!!.toString())
        startActivity(vista)
        Toast.makeText(this.baseContext, obj.getString("msj"), Toast.LENGTH_SHORT).show()

    }




}