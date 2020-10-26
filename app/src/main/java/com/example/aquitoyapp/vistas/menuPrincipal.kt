package com.example.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import org.json.JSONObject


class menuPrincipal : AppCompatActivity() {

    var datosUsuario: JSONObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        initView()

        findViewById<ImageButton>(R.id.btnTres).setOnClickListener {
            val vista = Intent(this, DomiciliosActivosActivity::class.java)
            startActivity(vista)
        }
        findViewById<ImageButton>(R.id.btnDos).setOnClickListener {
            val vista = Intent(this, DomiciliosDisponiblesActivity::class.java)
            //intent.putExtra("datos_usuario", datos_usuario.toString())
            //finish()
            startActivity(vista)
        }
        findViewById<ImageButton>(R.id.btnSeis).setOnClickListener {


        }
    }

    fun initView() {
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        val txtViewTres = findViewById<TextView>(R.id.textViewTres)
        txtViewTres.text = txtViewTres.text.toString() + datosUsuario!!.getString("usu_nombre")
    }

    fun btnTresAction() {

    }

    fun btnDosAction() {

    }

}