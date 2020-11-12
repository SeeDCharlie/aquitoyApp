package com.example.aquitoyapp.vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import org.json.JSONObject

class DomicilioActivoActivity : AppCompatActivity() {

    var datosUsuario: JSONObject? = null
    var datosDomicilio: JSONObject? = null
    var switchCamara = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domicilio_activo)


        initView()

        //eventos

        //tomar evidencias del inicio del domicilio
        findViewById<ImageButton>(R.id.btnDoAcAdduno).setOnClickListener {
            switchCamara = 1
            getFoto()
        }

        //tomar evidencias del final del domicilio

        findViewById<ImageButton>(R.id.btnDoAcAdddos).setOnClickListener {
            switchCamara = 0
            getFoto()
        }

        //boton que muestra la ubicacion en el mapa

        findViewById<ImageButton>(R.id.btnDoAcMap).setOnClickListener {
            var vistaMapa = Intent()
        }

    }

    //carga y asignacion de variables
    fun initView() {
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))

        var txtDesc = findViewById<TextView>(R.id.txtDoAcUno)
        var txtOrigen = findViewById<TextView>(R.id.txtDoAcDos)
        var txtDestino = findViewById<TextView>(R.id.txtDoAcTres)
        var txtCliente = findViewById<TextView>(R.id.txtDoAcCuatro)
        var txtFecha = findViewById<TextView>(R.id.txtDoAcCinco)
        var txtHora = findViewById<TextView>(R.id.txtDoAcSeis)

        txtDesc.text = txtDesc.text.toString() + datosDomicilio!!.getString("dom_descripcion")
        txtOrigen.text = txtOrigen.text.toString() + datosDomicilio!!.getString("dom_origen")
        txtDestino.text = txtDestino.text.toString() + datosDomicilio!!.getString("dom_destino")
        txtCliente.text = txtCliente.text.toString() + datosDomicilio!!.getString("cli_nombre")
        txtFecha.text = txtFecha.text.toString() + datosDomicilio!!.getString("dom_fechainicio")
        txtHora.text = txtHora.text.toString() + datosDomicilio!!.getString("dom_horainicio")

    }

    //funcion que llama la camara del sistema
    fun getFoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 1)
            }
        }
    }

    // oyente que captura la imagen tomada
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras?.get("data") as Bitmap
            val img = ImageView(this)
            img.setImageBitmap(imageBitmap)
            if (switchCamara == 1) {
                findViewById<LinearLayout>(R.id.lilDoAcUno).addView(img)
            } else if (switchCamara == 0) {
                findViewById<LinearLayout>(R.id.lilDoAcDos).addView(img)
            }
        }
    }
}