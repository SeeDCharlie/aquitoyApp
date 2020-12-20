package com.soportec.aquitoyapp.modelos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import okhttp3.*
import java.io.IOException

class VariablesConf {

    companion object {
        val BASE_URL_API = "https://soportec.co/mensajeria/webservices/"
        var CHECK_LOCATION = true
        val PERMISSION_CAM_GALLER_CODE = 1000
        val IMAGE_PICK_GALLERY_CODE = 1001
        val IMAGE_PICK_CAM_CODE = 1002
        val URL_UPLOAD_IMG = "https://soportec.co/mensajeria/webservices/guardarEvidencia.php"
        val SERVE_UPLOAD_DIRECTION_PATH = "http://soportec.co/mensajeria/webservices/uploads/"




    }



}