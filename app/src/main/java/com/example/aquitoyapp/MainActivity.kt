package com.example.aquitoyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import com.example.aquitoyapp.vistas.LogginActivity
import com.example.aquitoyapp.vistas.menuPrincipal
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var controldb: ControlSql? = null
    var controlapi: ControlApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controlapi = ControlApi(this)
        controldb = ControlSql(this)

        checkSesion()


    }

    //aqui se verifica que haya una sesion activa para ejecutar el resto de la aplicacion con los datos de la sesion
    fun checkSesion() {
        var query = "select * from sesiones where activo = 1 order by id desc;"
        var resultado = controldb?.getSessions(query)
        if (!resultado!!.isEmpty()) {
            controlapi!!.checkSesion(
                resultado.get(0).documento,
                resultado.get(0).contraseña,
                ::logginAction
            )

        } else {
            var vista = Intent(this, LogginActivity::class.java)
            finish()
            startActivity(vista)
        }
    }

    fun logginAction(datos_usuario: JSONObject) {
        val intent = Intent(this, menuPrincipal::class.java)
        intent.putExtra("datos_usuario", datos_usuario.toString())
        showMsj("Bienvenido " + datos_usuario.getString("usu_nombre"))
        finish()
        startActivity(intent)
    }


    //funcion que se debe ejecutar si la autenticacion del usuario es correcta


    fun showMsj(msj: String) {
        val showMsj = Toast.makeText(this.baseContext, msj, Toast.LENGTH_SHORT)
        showMsj.show()
    }
}