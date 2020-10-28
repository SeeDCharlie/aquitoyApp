package com.example.aquitoyapp.modelos

import android.content.Context
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.Serializable

class Api(var context: Context) : Serializable {

    private var baseUrl = "https://soportec.co/mensajeria/webservices/"
    private var requestExecute = Volley.newRequestQueue(this.context)


    fun respuestaPost(datos: JSONObject, direccion: String, funcion: (datos: JSONObject) -> Unit) {
        val url = this.baseUrl + direccion
        val request = JsonObjectRequest(
            Request.Method.POST, url, datos,
            { response ->
                try {
                    if (response.get("ok") == true) {
                        funcion(response.getJSONObject("dats"))
                    } else {
                        showMsj(response.getString("dats"))
                    }
                } catch (e: Exception) {
                    showMsj("Error: ${e}")
                }
            }, { error: VolleyError ->
                showMsj(">>>>>>>>>>>> \n Error!! ${error} \n Causa : ${error.cause}")
                println(">>>>>>>>>>>> \n Error!! ${error} \n Causa : ${error.cause}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0, 1f )
        this.requestExecute.add(request)

    }

    fun showMsj( msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.context, msj, duration)
        showMsj.show()
    }

}