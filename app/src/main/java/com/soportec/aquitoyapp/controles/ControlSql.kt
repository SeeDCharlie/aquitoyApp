package com.soportec.aquitoyapp.controles

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import com.soportec.aquitoyapp.modelos.DbLite
import com.soportec.aquitoyapp.modelos.Sesiones
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject
import java.io.Serializable


class ControlSql(var context: Context, sqltables :String = "")  {

    var motor_db: DbLite = DbLite(context, sqltables)

    // esta funcion solo inserta un registro a la vez, el parametro 'contenedorDatos' son los pares atributo - valor de 'tableName'
    fun insert(contenedorDatos: ContentValues, tableName: String):Long{
        val db = this.motor_db.writableDatabase
        val respuesta = db.insert(tableName, null, contenedorDatos)
        db.close()
        return respuesta
    }

    fun select(query:String):ArrayList<JSONObject>?{
        val db = this.motor_db.readableDatabase
        var datos = db.rawQuery(query, null)
        var dats: ArrayList<JSONObject> = ArrayList<JSONObject>()
        if (datos.moveToFirst()){
            do{
                var dAux = JSONObject()
                for (i: Int in 0..datos.columnNames.size - 1){
                    dAux.put(datos.getColumnName(i), datos.getString(i))
                }
                dats.add(dAux)
            }while(datos.moveToNext())
            return dats
        }else{
            return null
        }
    }

    fun selectForAtr(tableName: String, column:String, value: String): JSONObject?{
        var query = "select * from $tableName where $column = $value ;"
        val db = this.motor_db.readableDatabase
        var datos = db.rawQuery(query, null)
        datos.moveToFirst()
        if(datos.isFirst){
            var dats: JSONObject = JSONObject()
            for (i: Int in 0..datos.columnNames.size - 1){
                dats.put(datos.getColumnName(i), datos.getString(i))
            }
            return dats
        }else{
            return null
        }

    }


    fun insertsVals(inserts: JSONObject, tableName: String){
        for(register :String in inserts.keys()){
            val row:JSONObject = inserts.getJSONObject(register)
            val contentValues = ContentValues()
            for (col:String in row.keys()) {
                contentValues.put(col, row.getString(col))
            }
            insert( contentValues, tableName)
        }
    }

    fun deleteFromId(tableName:String, id:Int): Int {
        // IN instead of equal to compare multiple values
        val selection = "id IN (?)"
        val selectionArg = arrayOf("$id")
        val db = motor_db.writableDatabase
        val deletedRowsCount = db.delete(tableName, selection, selectionArg)
        db.close()
        return deletedRowsCount
    }



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

    fun getTableNames(): ArrayList<String>{
        var query = "SELECT name FROM sqlite_master WHERE type='table';"
        var taNames = ArrayList<String>()
        select(query)?.forEach {
            taNames.add(it.getString("name"))
        }
        return taNames
    }

}