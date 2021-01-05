package com.soportec.aquitoyapp.controles

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.vistas.LogginActivity
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import org.json.JSONObject
import java.io.File


class ControlMainActivity(ctx: Context, var activity: Activity) : apiInterfaz {

    var context: Context = ctx
    lateinit var controldb: ControlSql

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)



    // funcion que recupera la sentencia sql que crea las tablas en la db(sqlite) local del celular, solo si aun no existe la db
    fun getDbTables(){
        val dbFile: File = context.getDatabasePath("aqitoyDb")
        if (!dbFile.exists()){
            println("la base de datos no existe!!!")
            var datos = JSONObject()
            datos.put("getDbTables", true)
            peticionPost(datos, "getDbTables.php")
        }else {
            checkSesion()
        }

    }

    //aqui se verifica que haya una sesion activa para ejecutar el resto de la aplicacion con los datos de la sesion
    fun checkSesion() {
        controldb = ControlSql(context)
        var query = "select * from sesiones where activo = 1 order by id desc;"
        var resultado = controldb.getSessions(query)
        if (!resultado.isEmpty()) {
            val datos = JSONObject()
            datos.put("check_session", true)
            datos.put("documento", resultado.get(0).documento)
            datos.put("contrasena", resultado.get(0).contraseña)
            peticionPost(datos, "checkSession.php")
        } else {
            var vista = Intent(context, LogginActivity::class.java)
            activity.startActivity(vista)
            activity.finish()
        }
    }

    // metodo que inicia la vista de navegacion para el usuuario
    fun startView(datos: JSONObject){
        val vista = Intent(context, NavegacionActivity::class.java)
        vista.putExtra("datos_usuario", datos.toString())
        showMsj("Bienvenido " + datos.getString("usu_nombre"))
        //empezamos el proceso en segundo plano que notificara la ubicacion
        //checkStartLocation()
        activity.startActivity(vista)
        activity.finish()
    }
    // ------------------------------------------------------------------------------------
    fun showMsj(msj: String) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
    }

    override fun actionPost(datos: JSONObject) {

        when (datos.getString("tag")){

            "loggin" -> {
                startView(datos.getJSONObject("userDats"))
            }

            "getDbTables" -> {
                // se crea la base datos local
                var datsUpdate = datos.getJSONObject("updateID")
                var inserts = datos.getJSONObject("inserts")
                controldb = ControlSql(context, datos.getString("dbTables") )
                val valuesUpdate = ContentValues().apply {
                    put("id" , datsUpdate.getInt("id"))
                    put("update_date" , datsUpdate.getString("update_date"))
                    put("update_hour" , datsUpdate.getString("update_hour"))
                }
                controldb.insertsVals(inserts, "var_config")
                controldb.insert(valuesUpdate, "update_sqlite")
                checkSesion()
            }

        }

    }


    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        showMsj(obj.getString("msj"))
        activity.finish()

    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        showMsj(msj)
        activity.finish()
    }
}