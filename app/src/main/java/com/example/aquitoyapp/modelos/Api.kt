package com.example.aquitoyapp.modelos

import android.content.Context
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Api(var context: Context){

    private var baseUrl = "http://soportec.co/mensajeria/webservices/"
    private var requestExecute = Volley.newRequestQueue(this.context)


    fun respuestaPost(datos: JSONObject, direccion: String): JSONObject {
        val url = this.baseUrl + direccion
        var datos = JSONObject()
        val request = JsonObjectRequest(
            Request.Method.POST, url, datos,
            { response ->
                try {
                    if (response.get("ok") == true) {
                        datos.put("dats", response.get("dats"))
                        datos.put("ok", response.get("ok"))
                        showMsj("ok!")
                    } else {
                        showMsj("")
                    }
                } catch (e: Exception) {
                    showMsj("Exception: ${e}")
                }
            }, { error: VolleyError ->
                showMsj("Error!! $error.message")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0, 1f )
        this.requestExecute.add(request)
        return datos
    }



    fun showMsj( msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.context, msj, duration)
        showMsj.show()
    }

}