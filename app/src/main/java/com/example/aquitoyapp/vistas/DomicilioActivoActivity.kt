package com.example.aquitoyapp.vistas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import org.json.JSONObject


class DomicilioActivoActivity : AppCompatActivity() {

    var datosUsuario: JSONObject? = null
    var datosDomicilio: JSONObject? = null
    var switchCamara = -1
    var controlapi: ControlApi? = null
    var controldb: ControlSql? = null
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domicilio_activo)


        initView()

        //eventos

        //tomar evidencias del inicio del domicilio
        findViewById<ImageButton>(R.id.btnDoAcAdduno).setOnClickListener {
            switchCamara = 1
            getFoto()
        }

        //tomar evidencias del final del domicilio

        findViewById<ImageButton>(R.id.btnDoAcAdddos).setOnClickListener {
            switchCamara = 0
            getFoto()
        }

        //evento boton que muestra la ubicacion en el mapa

        findViewById<ImageButton>(R.id.btnDoAcMap).setOnClickListener {
            var vistaMapa = Intent()
        }

        //evento del boton para añadir una nueva nota al domicilio

        findViewById<ImageButton>(R.id.btnDoAcAddNote).setOnClickListener {
            var vista = Intent(this, NuevaNotaDom::class.java)
            vista.putExtra("datos_domicilio", datosDomicilio!!.toString())
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            startActivity(vista)
        }

        //boton que confirma que el domicilio esta terminado

        findViewById<ImageButton>(R.id.btnDoAcOk).setOnClickListener {
            controlapi!!.terminarDomicilio(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"),
                datosDomicilio!!.getInt("dom_id"),
                ::terminarDomicilio
            )
        }

        //boton para cancelar el domicilio

        findViewById<ImageButton>(R.id.btnDoAcCancel).setOnClickListener {
            controlapi!!.cancelarDomicilio(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"),
                datosDomicilio!!.getInt("dom_id"),
                ::terminarDomicilio
            )
        }
    }

    //carga y asignacion de variables
    fun initView() {
        controlapi = ControlApi(this, this)
        controldb = ControlSql(this)
        datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))

        var txtDesc = findViewById<TextView>(R.id.txtDoAcUno)
        var txtOrigen = findViewById<TextView>(R.id.txtDoAcDos)
        var txtDestino = findViewById<TextView>(R.id.txtDoAcTres)
        var txtCliente = findViewById<TextView>(R.id.txtDoAcCuatro)
        var txtFecha = findViewById<TextView>(R.id.txtDoAcCinco)
        var txtHora = findViewById<TextView>(R.id.txtDoAcSeis)

        txtDesc.text = txtDesc.text.toString() + datosDomicilio!!.getString("dom_descripcion")
        txtOrigen.text = txtOrigen.text.toString() + datosDomicilio!!.getString("dom_origen")
        txtDestino.text = txtDestino.text.toString() + datosDomicilio!!.getString("dom_destino")
        txtCliente.text = txtCliente.text.toString() + datosDomicilio!!.getString("cli_nombre")
        txtFecha.text = txtFecha.text.toString() + datosDomicilio!!.getString("dom_fechainicio")
        txtHora.text = txtHora.text.toString() + datosDomicilio!!.getString("dom_horainicio")

        cargarFotos(datosDomicilio!!.getInt("dom_id"))
    }

    //funcion que se ejecuta depues de que el domiciliario termina un domicilio

    fun terminarDomicilio(obj: JSONObject) {
        Toast.makeText(this, obj.getString("msj"), Toast.LENGTH_SHORT).show()
        finish()
    }

    //funcion que llama la camara del sistema
    fun getFoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val img = ImageView(this)
            var uriFile = data?.data
            img.setImageURI(data?.data)
            if (switchCamara == 1) {
                findViewById<LinearLayout>(R.id.lilDoAcUno).addView(img)
                controlapi!!.cargarEvidencia(
                    datosUsuario!!.getString("usu_documento"),
                    datosUsuario!!.getString("usu_pass"),
                    datosDomicilio!!.getInt("dom_id"),
                    getRealPathFromURI(uriFile!!)!!
                )
                controldb!!.addEviden(datosDomicilio!!.getInt("dom_id"), uriFile.toString(), 1)

            } else if (switchCamara == 0) {
                findViewById<LinearLayout>(R.id.lilDoAcDos).addView(img)
                controlapi!!.cargarEvidencia(
                    datosUsuario!!.getString("usu_documento"),
                    datosUsuario!!.getString("usu_pass"),
                    datosDomicilio!!.getInt("dom_id"),
                    getRealPathFromURI(uriFile!!)!!
                )
                controldb!!.addEviden(datosDomicilio!!.getInt("dom_id"), uriFile.toString(), 0)
            }
        }
    }


    private fun getRealPathFromURI(contentURI: Uri): String? {
        val filePath: String
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            filePath = contentURI.path!!.toString()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            filePath = cursor.getString(idx)
            cursor.close()
        }
        return filePath
    }

    private fun cargarFotos(id_dom: Int) {
        var query = "select uri from urievidencias where id_dom = $id_dom and origen_destino = 0;"
        var queryDos =
            "select uri from urievidencias where id_dom = $id_dom and origen_destino = 1;"
        var resultado = controldb?.getUriPhotosDomi(query)

        if (!resultado!!.isEmpty()) {
            resultado.forEach {
                val img = ImageView(this)
                var uriFile = Uri.parse(it)
                img.setImageURI(uriFile)
                findViewById<LinearLayout>(R.id.lilDoAcDos).addView(img)
            }

        }
        resultado = controldb?.getUriPhotosDomi(queryDos)
        if (!resultado!!.isEmpty()) {
            resultado.forEach {
                val img = ImageView(this)
                var uriFile = Uri.parse(it)
                img.setImageURI(uriFile)
                findViewById<LinearLayout>(R.id.lilDoAcUno).addView(img)
            }

        }


    }


}