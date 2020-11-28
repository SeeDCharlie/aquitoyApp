package com.soportec.aquitoyapp.controles

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.UploadInterfaz
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.temporal.ValueRange
import java.util.*

class ControlDomicilioActivo(var context: Context, var fragment: Fragment): apiInterfaz, UploadInterfaz {

    override var activity: Activity? = fragment.activity
    override var dialog: ProgressDialog? = null
    override var serverURL: String = VariablesConf.URL_UPLOAD_IMG
    override var serverUploadDirectoryPath: String = VariablesConf.SERVE_UPLOAD_DIRECTION_PATH
    override val client: OkHttpClient = OkHttpClient()

    var image_uri: Uri? = null
    var switchCamara = -1
    var controldb: ControlSql = ControlSql(context)
    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)




    //funcion que carga las fotos localmente de un domicilio que esta activo
    fun cargarFotos(id_dom: Int) {
        var query = "select uri from urievidencias where id_dom = $id_dom and origen_destino = 0;"
        var queryDos =
            "select uri from urievidencias where id_dom = $id_dom and origen_destino = 1;"
        var resultado = controldb?.getUriPhotosDomi(query)

        if (!resultado!!.isEmpty()) {
            resultado.forEach {
                val img = ImageView(context)
                var uriFile = Uri.parse(it)
                img.setImageURI(uriFile)
                fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcDos)?.addView(img)
            }

        }
        resultado = controldb?.getUriPhotosDomi(queryDos)
        if (!resultado!!.isEmpty()) {
            resultado.forEach {
                val img = ImageView(context)
                var uriFile = Uri.parse(it)
                img.setImageURI(uriFile)
                fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcUno)?.addView(img)
            }

        }
    }

    //funcion que devuelve la ruta url de una uri
    private fun getRealPathFromURI(contentURI: Uri): String? {
        val filePath: String
        val cursor: Cursor? = fragment.activity?.contentResolver?.query(contentURI, null, null, null, null)
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




    fun captureImg(){
        val img = ImageView(context)
        img.setImageURI(image_uri)
        if (switchCamara == 1) {
            fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcUno)?.addView(img)
            //secargan las evidencias al servidor
            cargarEvidencia(1 )
        } else if (switchCamara == 0) {
            fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcDos)?.addView(img)
//               //secconargan las evidencias al servidor
            cargarEvidencia(2)
        }
    }

    fun cargarEvidencia(code_evidencia:Int) {
        Toast.makeText(this.context, "cargando evidencia!!!", Toast.LENGTH_SHORT).show()
        var documento: String = NavegacionActivity.datosUsuario!!.getString("usu_documento")
        var contraseña: String = NavegacionActivity.datosUsuario!!.getString("usu_pass")
        var id_dom: Int = NavegacionActivity.domicilioAux!!.getInt("dom_id")
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val uploadName: String = "img_${id_dom}_${dateFormat}.jpeg"
        uploadFile(documento, contraseña, id_dom, code_evidencia, getRealPathFromURI(image_uri!!)!!, uploadName)
        //se guardan las rutas de las evidenciasen el una base de datos local
        controldb!!.addEviden(id_dom, image_uri!!.toString(), 1)
    }

    fun agregarNota(dialog:Dialog){

        dialog.setContentView(R.layout.dialog_add_note_dom)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnok = dialog.findViewById<Button>(R.id.btnAddNoDomOk)
        val btncancel = dialog.findViewById<Button>(R.id.btnAddNoDomCancel)
        //eventos dialog
        btnok.setOnClickListener{
            val notaText = dialog.findViewById<EditText>(R.id.txtAddNoDomNota).text
            val datos = JSONObject()
            datos.put("agregar_nota", true)
            datos.put("documento", NavegacionActivity.datosUsuario!!.getString("usu_documento"))
            datos.put("id_dom", NavegacionActivity.domicilioAux!!.getInt("dom_id"))
            datos.put("contraseña", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
            datos.put("nota", notaText)
            respuestaPost(datos, "agregarNota.php")
            dialog.dismiss()
        }
        btncancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()

    }

    override fun acionPots(obj: JSONObject) {
        super.acionPots(obj)

        if(obj.getString("tag") == "nota_agregada"){
            Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Snackbar.make(fragment.requireView(), msj, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

}