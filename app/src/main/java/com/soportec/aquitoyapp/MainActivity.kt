package com.soportec.aquitoyapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.soportec.aquitoyapp.controles.ControlMainActivity

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    lateinit var control: ControlMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        control = ControlMainActivity(this, this)
        pedirPermisoGeolocalizacion()

    }

    //pedir permisos de geolocalizacion

    fun pedirPermisoGeolocalizacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION  ) !== PackageManager.PERMISSION_GRANTED  ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION )) {
                ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1 )
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1 )
            }

        } else {
            control.getDbTables()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==  PackageManager.PERMISSION_GRANTED  ) {
                    if ((ContextCompat.checkSelfPermission( this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION  ) === PackageManager.PERMISSION_GRANTED) ) {
                        control.getDbTables()
                    }
                } else {
                    Toast.makeText(this, "Debes aceptar los permisos para usar la aplicacion", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }
}