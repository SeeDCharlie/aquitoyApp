package com.soportec.aquitoyapp.modelos

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.Serializable


@Suppress("DEPRECATION")
class Api(var context: Context, activity: Activity? = null) : Serializable {

    private var baseUrl = "https://soportec.co/mensajeria/webservices/"
    private var requestExecute = Volley.newRequestQueue(context)
    var activity = activity
    var dialog: ProgressDialog? = null
    var serverURL: String = "https://soportec.co/mensajeria/webservices/guardarEvidencia.php"
    var serverUploadDirectoryPath: String = "http://soportec.co/mensajeria/webservices/uploads/"
    val client = OkHttpClient()


    // funcion para el envio de una peticion POST
    fun respuestaPost(datos: JSONObject, direccion: String, funcion: (datos: JSONObject) -> Unit) {
        val url = this.baseUrl + direccion
        val request = JsonObjectRequest(
            Request.Method.POST, url, datos,
            { response ->
                try {
                    if (response.get("ok") == true) {
                        funcion(response.getJSONObject("dats"))
                    } else {
                        showMsj(response.getString("dats"))
                    }
                } catch (e: Exception) {
                    showMsj("Error: ${e}")
                }
            }, { error: VolleyError ->
                showMsj(">>>>>>>>>>>> \n Error!! ${error} \n Causa : ${error.cause}")
                println(">>>>>>>>>>>> \n Error!! ${error} \n Causa : ${error.cause}")
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1f
        )
        this.requestExecute.add(request)

    }


    fun showMsj(msj: String) {
        var duration = Toast.LENGTH_SHORT
        var showMsj = Toast.makeText(this.context, msj, duration)
        showMsj.show()
    }

    //--------------------------------------------------------------------------------------


    fun uploadFile(
        documento: String,
        contraseña: String,
        dom_id: Int,
        tipo_eviden: Int,
        sourceFilePath: String,
        uploadedFileName: String
    ) {
        Thread {
            val sourceFile = File(sourceFilePath)
            val mimeType = getMimeType(sourceFile)
            if (mimeType == null) {
                Log.e("file error", " >>>>>>Not able to get mime type")
            }
            val fileName: String =
                if (uploadedFileName == null) sourceFile.name else uploadedFileName
            toggleProgressDialog(true)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "uploaded_file", fileName,
                            sourceFile.asRequestBody(mimeType?.toMediaTypeOrNull())
                        ).addFormDataPart(
                            "documento", documento
                        ).addFormDataPart(
                            "contraseña", contraseña
                        ).addFormDataPart(
                            "guardar_evidencia", true.toString()
                        ).addFormDataPart(
                            "dom_id", dom_id.toString()
                        ).addFormDataPart(
                            "tipo_eviden", tipo_eviden.toString()
                        ).build()

                val request: okhttp3.Request =
                    okhttp3.Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("File upload", "success, path: $serverUploadDirectoryPath$fileName")
                    val jResponse = JSONObject(response.body!!.string()).getJSONObject("dats")
                    showToast(jResponse.getString("msj"))
                } else {
                    Log.e("File upload", "fallo al cargar la imagen")
                    showToast("Fallo al cargar la imagen!")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", ex.toString())
                showToast("File uploading failed : " + ex)
            }
            toggleProgressDialog(false)
        }.start()
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun showToast(message: String) {
        activity!!.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity!!.runOnUiThread {
            if (show) {
                @Suppress("DEPRECATION")
                dialog = ProgressDialog.show(activity, "Aquitoy Msj", "Cargando Imagen...", true)
            } else {
                dialog?.dismiss()
            }
        }
    }

}
