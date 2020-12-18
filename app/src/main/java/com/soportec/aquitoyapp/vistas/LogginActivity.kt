package com.soportec.aquitoyapp.vistas

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlSql
import com.soportec.aquitoyapp.controles.ReporteUbicacion
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class LogginActivity : AppCompatActivity(), apiInterfaz {

    var controldb: ControlSql? = null

    override var baseUrl: String = VariablesConf.BASE_URL_API

    override var requestExecute: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loggin)

        controldb = ControlSql(this)
        requestExecute = Volley.newRequestQueue(this)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            var datos = JSONObject()
            datos.put("loggin", true)
            datos.put("username", findViewById<EditText>(R.id.inpTextUser).text.toString())
            datos.put("password", findViewById<EditText>(R.id.inpTextPassword).text.toString())
            peticionPost(datos, "log_api.php")
        }

    }


    //funcion que se debe ejecutar si la autenticacion del usuario es correcta

    override fun actionPost(datos: JSONObject) {
        val intent = Intent(this, NavegacionActivity::class.java)
        var datos_usuario = datos.getJSONObject("userDats")
        controldb!!.addSession(

            datos_usuario.getInt("usu_id"),
            datos_usuario.getString("usu_correo"),
            datos_usuario.getString("usu_nombre"),
            datos_usuario.getString("usu_apellidos"),
            datos_usuario.getString("usu_documento"),
            datos_usuario.getString("usu_pass"),
            SimpleDateFormat("yyyy-mm-dd").format(Date())
        )

        intent.putExtra("datos_usuario", datos_usuario.toString())
        Toast.makeText(
            this,
            "Bienvenido " + datos_usuario.getString("usu_nombre"),
            Toast.LENGTH_SHORT
        ).show()
        finish()
        startActivity(intent)

    }

    override fun errorOk(obj: JSONObject) {
        Toast.makeText(this, obj.getString("msj"), Toast.LENGTH_SHORT).show()
    }

    override fun errorRequest(msj: String) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()
    }

}


