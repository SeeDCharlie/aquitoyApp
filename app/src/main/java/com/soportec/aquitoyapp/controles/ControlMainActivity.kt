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
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import com.soportec.aquitoyapp.vistas.LogginActivity
import org.json.JSONObject

class ControlMainActivity(ctx: Context, var activity: Activity) : apiInterfaz {

    var context: Context = ctx
    var controldb: ControlSql = ControlSql(context)

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    //empezamos el proceso en segundo plano que notificara la ubicacion
    init {
        val workRequest: WorkRequest = OneTimeWorkRequest.Builder(ReporteUbicacion::class.java)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
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

    fun showMsj(msj: String) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
    }

    override fun acionPots(datos_usuario: JSONObject) {
        val vista = Intent(context, NavegacionActivity::class.java)
        vista.putExtra("datos_usuario", datos_usuario.toString())
        showMsj("Bienvenido " + datos_usuario.getString("usu_nombre"))
        activity.startActivity(vista)
        activity.finish()
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