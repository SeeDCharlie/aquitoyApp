package com.example.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R


class menuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

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
    }

    fun btnTresAction() {

    }

    fun btnDosAction() {

    }

}