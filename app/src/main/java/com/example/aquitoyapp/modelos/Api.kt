package com.example.aquitoyapp.modelos

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Api (var context: Context){

    var baseUrl = "http://soportec.co/mensajeria/webservices"
    var requestExecute = Volley.newRequestQueue(this.context)


    public fun respuestaPost(datos : JSONObject, direccion:String) : JsonObjectRequest{
        val url = this.baseUrl + direccion
        val request = JsonObjectRequest(
            Request.Method.POST,url,datos,
            { response ->
                try{
                    var respuesta = JSONObject(response.get("dats").toString())
                    if(response.get("ok") == true){
                        showMsj("ok!")
                    }else {
                        showMsj("")
                    }
                }catch (e:Exception){
                    showMsj("Exception: ${e}")
                }
            }, { error: VolleyError ->
                showMsj("Error!! $error.message")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0, 1f )
        this.requestExecute.add(request)
        return request
    }



    fun showMsj( msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.context, msj, duration)
        showMsj.show()
    }

}