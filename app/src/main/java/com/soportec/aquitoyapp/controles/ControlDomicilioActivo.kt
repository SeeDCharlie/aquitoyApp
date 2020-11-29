package com.soportec.aquitoyapp.controles

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ControlDomicilioActivo(var context: Context, var fragment: Fragment): apiInterfaz, UploadInterfaz {

    override var activity: Activity? = fragment.activity
    override var dialog: ProgressDialog? = null
    override var serverURL: String = VariablesConf.URL_UPLOAD_IMG
    override var serverUploadDirectoryPath: String = VariablesConf.SERVE_UPLOAD_DIRECTION_PATH
    override val client: OkHttpClient = OkHttpClient()

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    var image_uri: Uri? = null
    var switchCamara = -1
    var controldb: ControlSql = ControlSql(context)

    private fun compressBitmap(bitmap:Bitmap, quality:Int):Bitmap{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    //funcion que carga las fotos localmente de un domicilio que esta activo
    fun cargarFotos(id_dom: Int) {
        var query = "select uri from urievidencias where id_dom = $id_dom and origen_destino = 2;"
        var queryDos =
            "select uri from urievidencias where id_dom = $id_dom and origen_destino = 1;"
        var resultado = controldb?.getUriPhotosDomi(query)

        if (!resultado!!.isEmpty()) {
            resultado.forEach {
                val img = ImageView(context)
                var uriFile = Uri.parse(it)
                var imgBitmap = MediaStore.Images.Media.getBitmap(fragment.requireActivity().getContentResolver(), uriFile)
                img.setImageBitmap(compressBitmap(imgBitmap,50))
                img.adjustViewBounds = true
                fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcDos)?.addView(img)
            }

        }
        resultado = controldb?.getUriPhotosDomi(queryDos)
        if (!resultado!!.isEmpty()) {
            resultado.forEach {
                val img = ImageView(context)
                var uriFile = Uri.parse(it)
                var imgBitmap = MediaStore.Images.Media.getBitmap(fragment.requireActivity().getContentResolver(), uriFile)
                img.setImageBitmap(compressBitmap(imgBitmap,50))
                img.adjustViewBounds = true
                fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcUno)?.addView(img)
            }

        }
    }

    //funcion que devuelve la ruta url de una uri
    private fun getRealPathFromURI(contentURI: Uri): String? {
        val filePath: String
        val cursor: Cursor? = fragment.activity?.contentResolver?.query(
            contentURI,
            null,
            null,
            null,
            null
        )
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
        var imgBitmap = MediaStore.Images.Media.getBitmap(fragment.requireActivity().getContentResolver(), image_uri)
        img.setImageBitmap(compressBitmap(imgBitmap,5))
        img.adjustViewBounds = true
        if (switchCamara == 1) {
            fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcUno)?.addView(img)
            //secargan las evidencias al servidor
            cargarEvidencia(switchCamara)
        } else if (switchCamara == 2) {
            fragment.view?.findViewById<LinearLayout>(R.id.lilDoAcDos)?.addView(img)
//               //secconargan las evidencias al servidor
            cargarEvidencia(switchCamara)
        }
    }
    //funciones de peticiones de a la api
    fun cargarEvidencia(code_evidencia: Int) {
        Toast.makeText(this.context, "cargando evidencia!!!", Toast.LENGTH_SHORT).show()
        var documento: String = NavegacionActivity.datosUsuario!!.getString("usu_documento")
        var contraseña: String = NavegacionActivity.datosUsuario!!.getString("usu_pass")
        var id_dom: Int = NavegacionActivity.domicilioAux!!.getInt("dom_id")
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val uploadName: String = "img_${id_dom}_${dateFormat}.jpeg"
        uploadFile(
            documento,
            contraseña,
            id_dom,
            code_evidencia,
            getRealPathFromURI(image_uri!!)!!,
            uploadName
        )
        //se guardan las rutas de las evidenciasen el una base de datos local

    }

    fun agregarNota(dialog: Dialog){

        dialog.setContentView(R.layout.dialog_add_note_dom)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnok = dialog.findViewById<Button>(R.id.btnAddNoDomOk)
        val btncancel = dialog.findViewById<Button>(R.id.btnAddNoDomCancel)
        //eventos dialog
        btnok.setOnClickListener{
            val notaText = dialog.findViewById<EditText>(R.id.txtAddNoDomNota).text
            val datos = JSONObject()
            println("datos domi : " + NavegacionActivity.domicilioAux!!.toString())
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

    fun cancelarDomicilio(){
        val datos = JSONObject()
        datos.put("cancelar_domicilio", true)
        datos.put("documento", NavegacionActivity.datosUsuario?.getString("usu_documento"))
        datos.put("id_dom", NavegacionActivity.domicilioAux?.getInt("dom_id"))
        datos.put("contraseña", NavegacionActivity.datosUsuario?.getString("usu_pass"))
        respuestaPost(datos, "cancelarDomicilio.php")
    }

    fun terminarDomicilio(){
        val datos = JSONObject()
        datos.put("terminar_domicilio", true)
        datos.put("documento", NavegacionActivity.datosUsuario?.getString("usu_documento"))
        datos.put("id_dom", NavegacionActivity.domicilioAux?.getInt("dom_id"))
        datos.put("contraseña", NavegacionActivity.datosUsuario?.getString("usu_pass"))
        respuestaPost(datos, "terminarDomicilio.php")
    }

    //manejo de respuestas a la api
    override fun acionPots(obj: JSONObject) {
        super.acionPots(obj)
        if(obj.getString("tag") == "nota_agregada"){
            Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        if (obj.getString("tag") == "terminar_dom"){
            Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            fragment.findNavController().navigate(R.id.action_domicilioActivoFrag_to_domiciliosDisponiblesFrag)

        }
        if(obj.getString("tag") == "cancelar_dom"){
            Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            fragment.findNavController().navigate(R.id.action_domicilioActivoFrag_to_domiciliosAvtivosFrag)
        }
    }


    ///manejo de errores de las peticiones a la api

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

    override fun despuesDeCargar(obj: JSONObject) {
        super.despuesDeCargar(obj)
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        controldb!!.addEviden(
            NavegacionActivity.domicilioAux!!.getInt("dom_id"),
            image_uri!!.toString(), switchCamara
        )
    }

    override fun errorOkCarga(obj: JSONObject) {
        super.errorOkCarga(obj)//getString("msj")
        Snackbar.make(fragment.requireView(), obj.toString(), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun errorRequestCarga(msj: String) {
        super.errorRequestCarga(msj)
        Snackbar.make(fragment.requireView(), msj, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

}