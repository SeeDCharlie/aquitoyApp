package com.soportec.aquitoyapp.controles

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import com.soportec.aquitoyapp.modelos.DbLite
import com.soportec.aquitoyapp.modelos.Sesiones
import java.io.Serializable


class ControlSql(var context: Context, sqltables :String = "")  {

    var motor_db: DbLite = DbLite(context, sqltables)


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
            put("apellidos", apellidos)
            put("documento", documento)
            put("contrasena", password)
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
                sesion_aux._id = cursor.getInt(cursor.getColumnIndex("id"))
                sesion_aux.id_user = cursor.getInt(cursor.getColumnIndex("id_user"))
                sesion_aux.email = cursor.getString(cursor.getColumnIndex("email"))
                sesion_aux.nombres = cursor.getString(cursor.getColumnIndex("nombres"))
                sesion_aux.apellidos = cursor.getString(cursor.getColumnIndex("apellidos"))
                sesion_aux.documento = cursor.getString(cursor.getColumnIndex("documento"))
                sesion_aux.contraseña = cursor.getString(cursor.getColumnIndex("contrasena"))
                sesion_aux.fecha_creacion =
                    cursor.getString(cursor.getColumnIndex("fecha_creacion"))
                sesion_aux.activo = cursor.getInt(cursor.getColumnIndex("activo"))
                list.add(sesion_aux)
            } while (cursor.moveToNext())
        }
        db.close()
        return list
    }
    //------------------------------------------------------------------------

    fun addEviden(
        id_dom: Int,
        uri: String,
        origen_destino: Int
    ): Long {
        val contenedorDatos = ContentValues().apply {
            put("id_dom", id_dom)
            put("uri", uri)
            put("origen_destino", origen_destino)
        }

        val db = this.motor_db.writableDatabase
        val respuesta = db.insert("urievidencias", null, contenedorDatos)
        db.close()
        return respuesta
    }

    fun getUriPhotosDomi(query: String): ArrayList<String> {
        val list: ArrayList<String> = ArrayList<String>()
        val db = this.motor_db.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("uri")))
            } while (cursor.moveToNext())
        }
        db.close()

        return list
    }

    fun actualizarDato(
        nombreTabla: String,
        valores: ContentValues,
        seleccion: String,
        argumentos_seleccion: Array<String>
    ): Boolean {
        try {
            val db = motor_db.writableDatabase
            //selection = "${FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
            //selectionArgs = arrayOf("MyOldTitle")
            val count = db.update(
                nombreTabla,
                valores,
                seleccion,
                argumentos_seleccion
            )
            db.close()
            return true
        } catch (error: Exception) {
            Toast.makeText(context, "error: ${error}", Toast.LENGTH_SHORT).show()
            return false

        }

    }

}