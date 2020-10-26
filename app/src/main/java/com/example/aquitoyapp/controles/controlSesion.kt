package com.example.aquitoyapp.controles

import android.content.Context
import com.example.aquitoyapp.modelos.Sesiones
import java.io.Serializable

class controlSesion(var context: Context) : Serializable {

    private var sesion = Sesiones()

    init {
        createFileSesion()
    }

    private fun createFileSesion() {
        val path = context.filesDir

        println("ruta de archivos : " + path.toString())

    }

    fun checkSesion(): Boolean {
        return true
    }
}