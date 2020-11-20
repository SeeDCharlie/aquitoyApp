package com.soportec.aquitoyapp.controles

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject


class ReporteUbicacion(
    appContext: Context, workerParams: WorkerParameters
) : Worker(appContext, workerParams), apiInterfaz {

    var context = appContext
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var lat: Double? = null
    var long: Double? = null
    var control_db = ControlSql(context)

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    //proceso que se ejecuta en segundo plano asi se reinicie el celular
    //este proceso se ejecuta cuando el manejador de eventos en segunto plano lo dicta
    //o cree que es conveniente
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        while (true) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    lat = location?.latitude
                    long = location?.longitude
                    sendLocation(lat!!, long!!)
                }
            println("coordenadas de ubicacions: $lat --- $long")
            Thread.sleep(1000 * 120)
        }
        return Result.success()
    }

    //esta funcion recibe latitud y longitud para ser registradas junto con un usuario si
    //este ha iniciado sesion
    fun sendLocation(lat: Double, long: Double) {
        var datos = JSONObject()
        datos.put("lat", lat)
        datos.put("long", long)
        datos.put("actualizar_ubicacion", true)

        var resultado =
            control_db.getSessions("select * from sesiones where activo = 1 order by id desc;")

        if (!resultado.isEmpty()) {
            datos.put("usu_id", resultado.get(0).id_user)
        } else {
            datos.put("usu_id", null)
        }

        respuestaPost(datos, "actualizarUbicacion.php")
    }

    override fun acionPots(obj: JSONObject) {
        println(obj.getString("msj"))
    }

    override fun errorOk(obj: JSONObject) {
        println("error : ${obj.getString("msj")}")
    }

    override fun errorRequest(msj: String) {
        println("error : ${msj}")
    }

}