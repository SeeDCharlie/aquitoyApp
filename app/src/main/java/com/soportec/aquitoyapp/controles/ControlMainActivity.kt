package com.soportec.aquitoyapp.controles

import android.app.Activity
import android.content.Context
import android.content.Intent
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
    var controldb: ControlSql = ControlSql(context)

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    //empezamos el proceso en segundo plano que notificara la ubicacion
    init {
        getDbTables()
        Thread.sleep(3500)
        val workRequest: WorkRequest = OneTimeWorkRequest.Builder(ReporteUbicacion::class.java)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)

    }
    // funcion que recupera la sentencia sql que crea las tablas en la db(sqlite) local del celular, solo si aun no existe la db
    fun getDbTables(){

        val dbFile: File = context.getDatabasePath("aqitoyDb")
        if (!dbFile.exists()){
            showMsj("la base de datos no existe!!!")
            var datos = JSONObject("{'getDbTables':true }")
            peticionPost(datos, "getDbTables.php")
        }
    }

    //aqui se verifica que haya una sesion activa para ejecutar el resto de la aplicacion con los datos de la sesion
    fun checkSesion() {
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
        activity.startActivity(vista)
        activity.finish()
    }
    // ------------------------------------------------------------------------------------
    fun showMsj(msj: String) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
    }

    override fun acionPots(datos: JSONObject) {

        when (datos.getString("tag")){

            "loggin" -> {
                startView(datos.getJSONObject("userDats"))
            }

            "getDbTables" -> {
                VariablesConf.SQLTABLES = datos.getString("dbTables")
            }

        }

    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        showMsj(msj)
        activity.finish()
    }
}