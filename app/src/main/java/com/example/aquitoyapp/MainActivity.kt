package com.example.aquitoyapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val controldb: ControlSql = ControlSql(this)
        val controlapi = ControlApi(this)

    }

    fun showMsj(msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this, msj, duration)
        showMsj.show()
    }
}