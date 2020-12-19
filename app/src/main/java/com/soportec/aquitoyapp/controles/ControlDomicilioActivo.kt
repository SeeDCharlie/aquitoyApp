package com.soportec.aquitoyapp.controles

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.*
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import okhttp3.OkHttpClient
import okhttp3.internal.notify
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ControlDomicilioActivo(var context: Context, var fragment: Fragment, evt: evtListEvid): apiInterfaz, UploadInterfaz {

    //variables de la interfaz para cargar inamgenes al servidor(UploadInterfaz)
    override var activity: Activity? = fragment.activity
    override var dialog: ProgressDialog? = null
    override var serverURL: String = VariablesConf.URL_UPLOAD_IMG
    override var serverUploadDirectoryPath: String = VariablesConf.SERVE_UPLOAD_DIRECTION_PATH
    override val client: OkHttpClient = OkHttpClient()

    //variables dela interfaz 'apiInterfaz'
    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)

    //variables de la clase
    var image_uri: Uri? = null
    var switchCamara = -1
    var controldb: ControlSql = ControlSql(context)

    var listAdapterOrigen: itemAdapterImageList? = null
    var listAdapterDestino:itemAdapterImageList? = null
    var listImgOrig: RecyclerView? = null
    var listImgDest: RecyclerView? = null
    var listImgOrigObj: ArrayList<modelImgEviden>? = null
    var listImgDestObj: ArrayList<modelImgEviden>? = null

    var evt:evtListEvid = evt

    init {
        listImgOrig = fragment.view?.findViewById(R.id.listImgOri)
        listImgDest = fragment.view?.findViewById(R.id.listImgDest)
        listImgOrig!!.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        listImgDest!!.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        listImgOrigObj = ArrayList<modelImgEviden>()
        listImgDestObj = ArrayList<modelImgEviden>()
        listAdapterOrigen = itemAdapterImageList(listImgOrigObj!!, evt)
        listImgOrig?.adapter = listAdapterOrigen
        listAdapterDestino= itemAdapterImageList(listImgDestObj!!, evt)
        listImgDest?.adapter = listAdapterDestino

    }

    //funcion que muestra la opcion de eiliminar a una evidencia

    fun popupMenuEvid(pocicion: Int, lista: ArrayList<modelImgEviden>, v: View){

        val popMenu = PopupMenu(context, v)
        popMenu.inflate(R.menu.menu_img_evid)
        popMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.deleteEviden -> {
                    showToast("imagen a eliminar, pocicion : ${pocicion}")
                    deleteEviden(pocicion, lista)
                    true
                }
                else -> true
            }
        }

        try {
            val pop = PopupMenu::class.java.getDeclaredField("mPopup")
            pop.isAccessible = true
            val menu = pop.get(popMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(
                menu,
                true
            )
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            popMenu.show()
        }


    }

    fun deleteEviden(pocicion: Int, lista: ArrayList<modelImgEviden>){
        var evidenModel = lista.get(pocicion)
        var type = controldb.select("select * from urievidencias where id = ${evidenModel.idImg} ")
        if(type != null){
            var datos = JSONObject()
            var t = type.get(0).getInt("origen_destino")
            datos.put("delete_evid", true)
            datos.put("documento", NavegacionActivity.datosUsuario?.getString("usu_documento"))
            datos.put("contrasena", NavegacionActivity.datosUsuario?.getString("usu_pass"))
            datos.put("id_dom", NavegacionActivity.domicilioAux?.getInt("dom_id"))
            datos.put("type_e", t)
            datos.put("poci", pocicion)

            peticionPost(datos, "deleteEviden.php")
        }

    }

    //funcion que carga las fotos localmente de un domicilio que esta activo
    fun cargarFotos(id_dom: Int) {
        listImgOrigObj!!.clear()
        listImgDestObj!!.clear()

        var query = "select * from urievidencias where id_dom = $id_dom order by id asc;"

        var resultado = controldb?.select(query)
        if (resultado != null) {
            println("alv cargando uno")
            showToast("id dom : ${id_dom} , num imgs : ${resultado.size}")
            resultado.forEach {
                println(it)
                cargarFoto(
                    it.getInt("origen_destino"),
                    Uri.parse(it.getString("uri")),
                    it.getInt("id")
                )
            }


        }
    }

    fun cargarFoto(origDest: Int, uri: Uri, idImg: Int){

        var bitMapReduce = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), uri)
        var bitmap = Bitmap.createScaledBitmap(bitMapReduce, 270, 210, false)

        var modelImg: modelImgEviden = modelImgEviden(bitmap, idImg)
        if(origDest == 1){
            listImgOrigObj!!.add(modelImg)
            listAdapterOrigen!!.notifyDataSetChanged()
        }else{
            listImgDestObj!!.add(modelImg)
            listAdapterDestino!!.notifyDataSetChanged()
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


    //funciones de peticiones a la api
    fun captureImg(code_evidencia: Int) {
        Toast.makeText(this.context, "cargando evidencia!!!", Toast.LENGTH_SHORT).show()
        var documento: String = NavegacionActivity.datosUsuario!!.getString("usu_documento")
        var contraseña: String = NavegacionActivity.datosUsuario!!.getString("usu_pass")
        var id_dom: Int = NavegacionActivity.domicilioAux!!.getInt("dom_id")
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val uploadName: String = "img_${id_dom}_${dateFormat}.jpeg"
        cargarFoto(switchCamara, image_uri!!, id_dom)
        uploadFile(
            documento,
            contraseña,
            id_dom,
            code_evidencia,
            getRealPathFromURI(image_uri!!)!!,
            uploadName,
            image_uri!!
        )
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
            datos.put("contrasena", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
            datos.put("nota", notaText)
            peticionPost(datos, "agregarNota.php")
            dialog.dismiss()
        }
        btncancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }



    fun terminarDomicilio(dialog: Dialog){

        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnok = dialog.findViewById<Button>(R.id.btnDiCoOk)
        val btncancel = dialog.findViewById<Button>(R.id.btnDiCoCancel)

        btnok.setOnClickListener{
            val datos = JSONObject()
            datos.put("terminar_domicilio", true)
            datos.put("documento", NavegacionActivity.datosUsuario?.getString("usu_documento"))
            datos.put("id_dom", NavegacionActivity.domicilioAux?.getInt("dom_id"))
            datos.put("contrasena", NavegacionActivity.datosUsuario?.getString("usu_pass"))
            peticionPost(datos, "terminarDomicilio.php")
            dialog.dismiss()
        }

        btncancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    fun deleteEvidFromPhone(idImg: Int){
        var dats = controldb.select("select * from urievidencias where id = $idImg");
        var msj = ""
        if( dats != null) {
            var uri = dats.get(0).getString("uri")
            if (controldb.deleteFromId("urievidencias", idImg) > 0) {
                try {
                    var file_dj_path = getRealPathFromURI(Uri.parse(uri))
                    val fdelete: File = File(file_dj_path)
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            msj = "file Deleted :$file_dj_path"
                        } else {
                            msj = "file not Deleted :$file_dj_path"
                        }
                    }
                }catch (e: java.lang.Exception){
                    msj = "no se elimino la imagen del celular ${e.message}"
                }
            }
            else{
                msj = "la img no se elimino de la sqlite!!!!!"
            }
        }else{
            msj = "no existe la img en db sqlite"
        }
        Snackbar.make(fragment.requireView(), msj, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }

    //manejo de respuestas a la api

    //funcion que se ejecuta cuando el servidor ha dado una respuesta correcta
    //devolviendo un objeto json con informacion
    override fun actionPost(obj: JSONObject) {
        super.actionPost(obj)
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
        if(obj.getString("tag") == "delete_evid"){
            var poci = obj.getInt("poci")
            if(obj.getInt("type") == 1){
                var modelImg = listImgOrigObj!!.get(poci)
                deleteEvidFromPhone(modelImg.idImg)
                listImgOrigObj!!.remove(modelImg)
                listAdapterOrigen!!.notifyItemRemoved(poci)

            }else{
                var modelImg = listImgDestObj!!.get(poci)
                deleteEvidFromPhone(modelImg.idImg)
                listImgDestObj!!.remove(modelImg)
                listAdapterDestino!!.notifyItemRemoved(poci)
            }
        }

    }
    //esta funcion se ejecuta cuando la carga de una imagen ha sido correcta
    // y el servidor a dado una respuesta. la informacion devueta por el servidor
    //viene en un objeto json
    override fun despuesDeCargar(obj: JSONObject) {
        super.despuesDeCargar(obj)
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

        var id = controldb!!.addEviden(
            NavegacionActivity.domicilioAux!!.getInt("dom_id"),
            image_uri!!.toString(), switchCamara
        )
        if(switchCamara == 1){
            listImgOrigObj!!.get(listImgOrigObj!!.size-1).idImg = id.toInt()
        }else{
            listImgDestObj!!.get(listImgDestObj!!.size-1).idImg = id.toInt()
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




