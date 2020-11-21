package com.soportec.aquitoyapp.modelos


import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

interface apiInterfaz {

    var baseUrl: String
    var requestExecute: RequestQueue?

    // funcion para el envio de una peticion POST
    fun respuestaPost(datos: JSONObject, direccion: String) {
        val url = this.baseUrl + direccion
        val request = JsonObjectRequest(
            Request.Method.POST, url, datos,
            { response ->
                if (response.get("ok") == true) {
                    acionPots(response.getJSONObject("dats"))
                } else {
                    errorOk(response.getJSONObject("dats"))
                }
            }, { error: VolleyError ->
                errorRequest(error.toString())
                println(">>>>>>>>>>>> \n Error!! ${error} \n Causa : ${error.cause}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1f
        )
        this.requestExecute!!.add(request)

    }

    fun acionPots(obj: JSONObject) {

    }

    fun errorOk(obj: JSONObject) {
        println("error : ${obj.getString("msj")}")
    }
    fun errorRequest(msj: String) {
        println("error : ${msj}")
    }

}