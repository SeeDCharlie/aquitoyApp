package com.example.aquitoyapp.modelos

import android.app.Activity
import android.app.ProgressDialog
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UploadUtility(activity: Activity) {

    var activity = activity
    var dialog: ProgressDialog? = null
    var serverURL: String = "https://soportec.co/mensajeria/webservices/guardarEvidencia.php"
    var serverUploadDirectoryPath: String = "http://soportec.co/mensajeria/webservices/uploads"
    val client = OkHttpClient()

    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null) {
        showToast("cargando img\n${sourceFilePath}")

        uploadFile(File(sourceFilePath), uploadedFileName)
    }


    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) {
        Thread {
            val mimeType = getMimeType(sourceFile)
            if (mimeType == null) {
                showToast("file error >>>>>>Not able to get mime type")
            }
            val fileName: String =
                if (uploadedFileName == null) sourceFile.name else uploadedFileName
            toggleProgressDialog(true)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "uploaded_file",
                            fileName,
                            sourceFile.asRequestBody(mimeType?.toMediaTypeOrNull())
                        )
                        .build()

                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("File upload", "success, path: $serverUploadDirectoryPath$fileName")
                    showToast("File uploaded successfully at $serverUploadDirectoryPath$fileName")
                } else {
                    Log.e("File upload", "fallo al cargar laimagen")
                    showToast("File uploading failed  ")
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
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                @Suppress("DEPRECATION")
                dialog = ProgressDialog.show(activity, "", "Cargando Imagen...", true)
            } else {
                dialog?.dismiss()
            }
        }
    }

}