package com.example.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class LogginActivity : AppCompatActivity() {

    var controldb: ControlSql? = null
    var controlapi: ControlApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loggin)

        controlapi = ControlApi(this)
        controldb = ControlSql(this)


        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            val username = findViewById<EditText>(R.id.inpTextUser).text.toString()
            val password = findViewById<EditText>(R.id.inpTextPassword).text.toString()
            controlapi!!.loggin(username, password, ::btnLogginAction)

        }

    }


    //funcion que se debe ejecutar si la autenticacion del usuario es correcta

    fun btnLogginAction(datos_usuario: JSONObject) {
        val intent = Intent(this, menuPrincipal::class.java)

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
        showMsj("Bienvenido " + datos_usuario.getString("usu_nombre"))
        finish()
        startActivity(intent)
    }


    fun showMsj(msj: String) {
        val showMsj = Toast.makeText(this.baseContext, msj, Toast.LENGTH_SHORT)
        showMsj.show()
    }
}
