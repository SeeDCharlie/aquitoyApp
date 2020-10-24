package com.example.aquitoyapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql


class MainActivity : AppCompatActivity() {
    val controldb: ControlSql = ControlSql(this)
    val controlapi = ControlApi(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.inpTextUser)
        val password = findViewById<EditText>(R.id.inpTextPassword)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            btnLogginAction(username.text.toString(), password.text.toString())
        }

    }


    fun btnLogginAction(username: String, password: String) {
        if (this.controlapi.loggin(username, password)) {
            println("")
        }

    }

    fun showMsj(msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this, msj, duration)
        showMsj.show()
    }
}