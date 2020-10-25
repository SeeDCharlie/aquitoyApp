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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var controldb = ControlSql(this)
        var controlapi = ControlApi(this)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            //btnLogginAction(controlapi)
            val username = findViewById<EditText>(R.id.inpTextUser).text.toString()
            val password = findViewById<EditText>(R.id.inpTextPassword).text.toString()
            controlapi.loggin(username, password, ::btnLogginAction)

        }

    }

    fun btnLogginAction(datos_usuario: JSONObject) {
        val intent = Intent(this, menuPrincipal::class.java)
        intent.putExtra("datos_usuario", datos_usuario.toString())
        startActivity(intent)
    }


    fun showMsj(msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.baseContext, msj, duration)
        showMsj.show()
    }
}