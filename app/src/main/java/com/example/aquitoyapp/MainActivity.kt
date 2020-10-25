package com.example.aquitoyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import com.example.aquitoyapp.vistas.menuPrincipal
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var controldb: ControlSql? = null
    var controlapi: ControlApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controldb = ControlSql(this)
        controlapi = ControlApi(this)

        checkSesion()

        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            //btnLogginAction(controlapi)
            val username = findViewById<EditText>(R.id.inpTextUser).text.toString()
            val password = findViewById<EditText>(R.id.inpTextPassword).text.toString()
            controlapi!!.loggin(username, password, ::btnLogginAction)

        }

    }

    fun checkSesion() {
        var query = "select * from sesiones where activo = 1 order by desc;"
        var resultado = controldb?.getSessions(query)
        if (resultado!!.count() > 0) {
            var sesionAux = resultado.get(0)
            val intent = Intent(this, menuPrincipal::class.java)
            showMsj("sesion to string : " + sesionAux.toString())
            intent.putExtra("datos_usuario", sesionAux.toString())
            finish()
            startActivity(intent)
        }
    }

    fun btnLogginAction(datos_usuario: JSONObject) {
        val intent = Intent(this, menuPrincipal::class.java)
        controldb!!.addSession(
            datos_usuario.getInt("user_id"),
            datos_usuario.getString("nombre_usuario"),
            datos_usuario.getString("email"),
            datos_usuario.getString("contraseña"),
            datos_usuario.getString("fecha"),
        )
        intent.putExtra("datos_usuario", datos_usuario.toString())
        finish()
        startActivity(intent)
    }


    fun showMsj(msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.baseContext, msj, duration)
        showMsj.show()
    }
}