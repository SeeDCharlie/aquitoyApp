package com.soportec.aquitoyapp.modelos


import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

interface apiInterfaz {

    var baseUrl: String
    var requestExecute: RequestQueue?

    // funcion para el envio de una peticion POST
    fun peticionPost(datos: JSONObject, direccion: String) {
        val url = this.baseUrl + direccion
        val request = JsonObjectRequest(
            Request.Method.POST, url, datos,
            { response ->
                if (response.get("ok") == true) {
                    acionPost(response.getJSONObject("dats"))
                } else {
                    errorOk(response.getJSONObject("dats"))
                }
            }, { error: VolleyError ->
                errorRequest("Compruebe su conexion a internet")
                println(">>>>>>>>>>>> \n Error!! ${error} \n Causa : ${error.cause}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1f
        )
        this.requestExecute!!.add(request)
    }

    // poticion GET
    fun peticionGet(direccion :String){

        val request = okhttp3.Request.Builder()
            .url(VariablesConf.BASE_URL_API + direccion)
            .build()
        val client: OkHttpClient = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                errorRequest(e.message!!.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    var dats = JSONObject()
                    if (response.isSuccessful) {
                        dats.put("dats", response.body!!)
                        acionPost(dats)
                    }else{
                        dats.put("msj", response.headers.get("msj"))
                        errorOk(dats)
                    }
                }
            }
        })

    }



    //------------------------------------------------------------------------------------------
    // metodos para las respuestas de las peticiones POST Y GET

    fun acionPost(obj: JSONObject) {
    }

    fun errorOk(obj: JSONObject) {
        println("error : ${obj.getString("msj")}")
    }
    fun errorRequest(msj: String) {
        println("error : ${msj}")
    }

}