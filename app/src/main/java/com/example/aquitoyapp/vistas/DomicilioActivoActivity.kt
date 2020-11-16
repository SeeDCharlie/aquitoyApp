package com.example.aquitoyapp.vistas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle

import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.aquitoyapp.R
import com.example.aquitoyapp.controles.ControlApi
import com.example.aquitoyapp.controles.ControlSql
import com.example.aquitoyapp.modelos.apiInterfaz
import okhttp3.OkHttpClient

import org.json.JSONObject


class DomicilioActivoActivity : AppCompatActivity(),
    apiInterfaz {

    var datosUsuario: JSONObject? = null
    var datosDomicilio: JSONObject? = null
    var switchCamara = -1
    var controlapi: ControlApi? = null
    var controldb: ControlSql? = null
    var image_uri: Uri? = null

    override var context: Context = this
    override var activity: Activity = this

    override var baseUrl = "https://soportec.co/mensajeria/webservices/"
    override var requestExecute = Volley.newRequestQueue(context)

    override var dialog: ProgressDialog? = null
    override var serverURL: String =
        "https://soportec.co/mensajeria/webservices/guardarEvidencia.php"
    override var serverUploadDirectoryPath: String =
        "http://soportec.co/mensajeria/webservices/uploads/"
    override val client = OkHttpClient()


    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001


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
            //var vistaMapa = Intent()
        }

        //evento del boton para añadir una nueva nota al domicilio

        findViewById<ImageButton>(R.id.btnDoAcAddNote).setOnClickListener {
            var vista = Intent(this, NuevaNotaDom::class.java)
            vista.putExtra("datos_domicilio", datosDomicilio!!.toString())
            vista.putExtra("datos_usuario", datosUsuario!!.toString())
            startActivity(vista)
        }

        //boton que confirma que el domicilio esta terminado
/*
        findViewById<ImageButton>(R.id.btnDoAcOk).setOnClickListener {
            controlapi!!.terminarDomicilio(
                datosUsuario!!.getString("usu_documento"),
                datosUsuario!!.getString("usu_pass"),
                datosDomicilio!!.getInt("dom_id"),
                ::terminarDomicilio
            )
        }    */

        //boton que guarda avances de domicilio activo

        findViewById<ImageButton>(R.id.btnDoAcOk).setOnClickListener {

            controlapi!!.guardarDomicilio(
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

    //funcion que pide permisos a los usuarios para utilizar la camara
    //y abrir la camara para luegorecuperar la imagen tomada
    fun getFoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system os is < marshmallow
            pickImageFromGallery()
        }

    }

    //funcion que inicia la camara para tomar una evidencia
    private fun pickImageFromGallery() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    //funcion que pide permisos para acceder a la galeria de imagenes
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
                    //permission from popup was granted
                    pickImageFromGallery()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //oyente que captura la imagen seleccionada de la camara
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK /*&& requestCode == IMAGE_PICK_CODE*/) {
            val img = ImageView(this)
            var uriFile = image_uri
            img.setImageURI(uriFile)
            if (switchCamara == 1) {
                findViewById<LinearLayout>(R.id.lilDoAcUno).addView(img)
                //secargan las evidencias al servidor
                controlapi!!.cargarEvidencia(
                    datosUsuario!!.getString("usu_documento"),
                    datosUsuario!!.getString("usu_pass"),
                    datosDomicilio!!.getInt("dom_id"),
                    1,
                    getRealPathFromURI(uriFile!!)!!
                )
                //se guardan las rutas de las evidenciasen el una base de datos local
                controldb!!.addEviden(datosDomicilio!!.getInt("dom_id"), uriFile.toString(), 1)

            } else if (switchCamara == 0) {
                findViewById<LinearLayout>(R.id.lilDoAcDos).addView(img)
//               //secargan las evidencias al servidor
                controlapi!!.cargarEvidencia(
                    datosUsuario!!.getString("usu_documento"),
                    datosUsuario!!.getString("usu_pass"),
                    datosDomicilio!!.getInt("dom_id"),
                    2,
                    getRealPathFromURI(uriFile!!)!!
                )
                //se guardan las rutas de las evidenciasen en una base de datos local
                controldb!!.addEviden(datosDomicilio!!.getInt("dom_id"), uriFile.toString(), 0)
            }
        }
    }

    //funcion que devuelve la ruta url de una uri
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

    //funcion que carga las fotos localmente de un domicilio que esta activo
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


    //nuevo metodo que se ejecuta despues deuna llamada a la api

    override fun acionPots(obj: JSONObject) {
        TODO("Not yet implemented")
    }


}