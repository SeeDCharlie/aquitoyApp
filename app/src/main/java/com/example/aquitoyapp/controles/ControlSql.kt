package com.example.aquitoyapp.controles

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.aquitoyapp.modelos.DbLite
import com.example.aquitoyapp.modelos.Sesiones


class ControlSql(var context: Context) {

    val motor_db: DbLite  = DbLite(context)


    fun addSession(username: String, email: String, password: String, fecha: String): Long{
        val contenedorDatos = ContentValues().apply {
            put("email", email)
            put("nombre_usuario", username)
            put("contraseña", password)
            put("fecha_creacion", fecha)
            put("activo", 1)
        }

        val db = this.motor_db.writableDatabase
        val respuesta = db.insert("sesiones", null, contenedorDatos)
        db.close()

        return respuesta
    }

    fun getSessions(): MutableList<Sesiones> {
        val list: MutableList<Sesiones> = ArrayList()
        val db = this.motor_db.readableDatabase
        val query = "Select * from sesiones"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val ses = Sesiones()
                ses.id = result.getString(result.getColumnIndex("id")).toInt()
                ses.email = result.getString(result.getColumnIndex("email"))
                ses.nombre_usuario = result.getString(result.getColumnIndex("nombre_usuario"))
                list.add(ses)
            }
            while (result.moveToNext())
        }
        return list
    }


}