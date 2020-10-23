package com.example.aquitoyapp.modelos

import android.content.Intent
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class Api {

    var baseUrl = "http://soportec.co/mensajeria/webservices"



    val url = "http://192.168.1.56/pruebaWebServices/index.php"

    val jsonObject = JSONObject()

    val request = JsonObjectRequest(
        Request.Method.POST,url,jsonObject,
        { response ->
            try{
                if(response.get("ok") == true){
                    var userName = JSONObject(JSONObject(response.get("dats").toString()).get("0").toString()).get("username")
                    showMsj("Bienvenido ${userName}")
                    val intent = Intent(this, mainframe::class.java)
                    intent.putExtra("username",username)
                    startActivity(intent)
                }else{
                    showMsj("Usuario o contraseña incorrectos!!!")
                }
            }catch (e:Exception){
                showMsj("Exception: ${e}")
            }
        }, { error: VolleyError ->
            showMsj("Error!! $error.message")
        })

    request.retryPolicy = DefaultRetryPolicy(
    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0, 1f )

    return request

}