package com.example.aquitoyapp.controles

import android.content.ContentValues
import android.content.Context
import com.example.aquitoyapp.modelos.DbLite
import com.example.aquitoyapp.modelos.Sesiones
import java.io.Serializable


class ControlSql(var context: Context) : Serializable {

    val motor_db: DbLite = DbLite(context)


    //metodo para agregar una sesion activa
    fun addSession(
        id_user: Int,
        email: String,
        nombres: String,
        apellidos: String,
        documento: String,
        password: String,
        fecha: String
    ): Long {
    val contenedorDatos = ContentValues().apply {
        put("id_user", id_user)
        put("email", email)
        put("nombres", nombres)
        put("apellidos", nombres)
        put("documento", nombres)
        put("contraseña", password)
        put("fecha_creacion", fecha)
        put("activo", 1)
    }

    val db = this.motor_db.writableDatabase
        val respuesta = db.insert("sesiones", null, contenedorDatos)
        db.close()

        return respuesta
    }

    // metodo de prueba que devuelve todas las sesiones guardadas
    fun getSessions(query: String): ArrayList<Sesiones> {
        val list: ArrayList<Sesiones> = ArrayList<Sesiones>()
        val db = this.motor_db.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val sesion_aux = Sesiones()
                sesion_aux._id = cursor.getInt(cursor.getColumnIndex("_id"))
                sesion_aux.id_user = cursor.getInt(cursor.getColumnIndex("id_user"))
                sesion_aux.email = cursor.getString(cursor.getColumnIndex("email"))
                sesion_aux.nombres = cursor.getString(cursor.getColumnIndex("nombres"))
                sesion_aux.apellidos = cursor.getString(cursor.getColumnIndex("apellidos"))
                sesion_aux.documento = cursor.getString(cursor.getColumnIndex("documento"))
                sesion_aux.contraseña = cursor.getString(cursor.getColumnIndex("contraseña"))
                sesion_aux.fecha_creacion =
                    cursor.getString(cursor.getColumnIndex("fecha_creacion"))
                sesion_aux.activo = cursor.getInt(cursor.getColumnIndex("activo"))
                list.add(sesion_aux)
            } while (cursor.moveToNext())
        }
        return list
    }


}