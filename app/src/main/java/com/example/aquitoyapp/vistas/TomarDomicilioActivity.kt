package com.example.aquitoyapp.vistas

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import org.json.JSONObject

class TomarDomicilioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomar_domicilio)

        val datosDomi = JSONObject(intent.getStringExtra("datosDomi"))

        val tvUno = findViewById<TextView>(R.id.tvToDoOrigen)
        val tvDos = findViewById<TextView>(R.id.tvToDoDestino)
        val tvTres = findViewById<TextView>(R.id.tvToDoDescripcion)
        val tvCuatro = findViewById<TextView>(R.id.tvToDoFechaAsig)
        val tvCinco = findViewById<TextView>(R.id.tvToDoCliente)
        val tvSeis = findViewById<TextView>(R.id.tvToDoNotas)

        tvUno.text = tvUno.text.toString() + datosDomi.getString("dom_origen")
        tvDos.text = tvDos.text.toString() + datosDomi.getString("dom_destino")
        tvTres.text = tvTres.text.toString() + datosDomi.getString("dom_descripcion")
        tvCuatro.text = tvCuatro.text.toString() + datosDomi.getString("dom_fechaasignacion")
        tvCinco.text = tvCinco.text.toString() + datosDomi.getString("cli_nombre")
        tvSeis.text = tvSeis.text.toString() + datosDomi.getString("dom_notas")


    }


}