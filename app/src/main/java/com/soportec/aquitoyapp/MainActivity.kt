package com.soportec.aquitoyapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest

import com.soportec.aquitoyapp.controles.ControlApi
import com.soportec.aquitoyapp.controles.ControlSql
import com.soportec.aquitoyapp.controles.ReporteUbicacion
import com.soportec.aquitoyapp.vistas.LogginActivity
import com.soportec.aquitoyapp.vistas.menuPrincipal
import org.json.JSONObject


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    var controldb: ControlSql? = null
    var controlapi: ControlApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controlapi = ControlApi(this)
        controldb = ControlSql(this)

        pedirPermisoGeolocalizacion()


    }

    //pedir permisos de geolocalizacion

    fun pedirPermisoGeolocalizacion() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }

        } else {
            val workRequest: WorkRequest = OneTimeWorkRequest.Builder(ReporteUbicacion::class.java)
                .build()
            WorkManager.getInstance(this).enqueue(workRequest)
            checkSesion()
        }
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

    //funcion que se ejecuta si hay una sesion iniciada en el celular

    fun logginAction(datos_usuario: JSONObject) {
        val intent = Intent(this, menuPrincipal::class.java)
        intent.putExtra("datos_usuario", datos_usuario.toString())
        showMsj("Bienvenido " + datos_usuario.getString("usu_nombre"))
        finish()
        startActivity(intent)
    }


    fun showMsj(msj: String) {
        val showMsj = Toast.makeText(this.baseContext, msj, Toast.LENGTH_SHORT)
        showMsj.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        val workRequest: WorkRequest =
                            OneTimeWorkRequest.Builder(ReporteUbicacion::class.java)
                                .build()
                        WorkManager.getInstance(this).enqueue(workRequest)
                        checkSesion()

                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}