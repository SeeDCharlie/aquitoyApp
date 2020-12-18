package com.soportec.aquitoyapp.modelos

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File

interface UploadInterfaz {


    var activity: Activity?
    var dialog: ProgressDialog?
    var serverURL: String
    var serverUploadDirectoryPath: String
    val client: OkHttpClient
    //--------------------------------------------------------------------------------------


    fun uploadFile(
        documento: String,
        contraseña: String,
        dom_id: Int,
        tipo_eviden: Int,
        sourceFilePath: String,
        uploadedFileName: String,
        uriImg: Uri
    ) {
        Thread {
            var bitMapReduce = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), uriImg)
            val sourceFile = File(sourceFilePath)
            sourceFile.writeBitmap(bitMapReduce, Bitmap.CompressFormat.JPEG, 50)
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
                            "contrasena", contraseña
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
                    val jResponse = JSONObject(response.body!!.string())
                    if(jResponse.getBoolean("ok")){
                        despuesDeCargar(jResponse.getJSONObject("dats") )
                    }else{
                        errorOkCarga(jResponse.getJSONObject("dats") )
                    }
                } else {
                    Log.e("File upload", "fallo al cargar la imagen")
                    errorRequestCarga("Fallo al cargar la imagen!\n Vuelva a intentarlo o reporte su problema")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", ex.toString())
                showToast("Error al cargar la imagen :\n compruebe su conexion a internet\n" + ex)
            }
            toggleProgressDialog(false)
        }.start()
    }

   fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity?.runOnUiThread {
            if (show) {
                @Suppress("DEPRECATION")
                dialog = ProgressDialog.show(activity, "Aquitoy Msj", "Cargando Evidencia...", true)
            } else {
                dialog?.dismiss()
            }
        }
    }

    fun despuesDeCargar(obj:JSONObject){

    }

    fun errorOkCarga(obj: JSONObject){

    }
    fun errorRequestCarga(msj:String){

    }


}