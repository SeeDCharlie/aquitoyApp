package com.example.aquitoyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.controlSesion
import com.example.aquitoyapp.vistas.menuPrincipal
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var control_sesion: controlSesion? = null
    var controlapi: ControlApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        control_sesion = controlSesion(this)
        controlapi = ControlApi(this)

        checkSesion()

        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            //btnLogginAction(controlapi)
            val username = findViewById<EditText>(R.id.inpTextUser).text.toString()
            val password = findViewById<EditText>(R.id.inpTextPassword).text.toString()
            controlapi!!.loggin(username, password, ::btnLogginAction)

        }

    }

    //aqui se verifica que haya una sesion activa para ejecutar el resto de la aplicacion con los datos de la sesion
    fun checkSesion() {
        var query = "select * from userSession.sesiones where activo = 1 order by desc;"
        //var resultado = controldb?.getSessions(query)
        //var resultado = control_sesion.checkSesion()
        /*if (resultado) {
            val intent = Intent(this, menuPrincipal::class.java)
            showMsj("sesion to string : " + sesionAux.toString())
            intent.putExtra("datos_usuario", sesionAux.toString())
            finish()
            startActivity(intent)
        }*/
    }

    fun btnLogginAction(datos_usuario: JSONObject) {
        val intent = Intent(this, menuPrincipal::class.java)
        val dats = datos_usuario.getJSONObject("dats")

        /*controldb!!.addSession(
            dats.getInt("user_id"),
            dats.getString("usu_correo"),
            dats.getString("usu_nombre"),
            dats.getString("usu_apellido"),
            dats.getString("usu_dosumento"),
            dats.getString("usu_pass"),
            SimpleDateFormat("yyyy-MM-dd").format(Date())
        )*/
        intent.putExtra("datos_usuario", datos_usuario.toString())
        showMsj("datos_usuario" + dats.toString())
        finish()
        startActivity(intent)
    }


    fun showMsj(msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.baseContext, msj, duration)
        showMsj.show()
    }
}