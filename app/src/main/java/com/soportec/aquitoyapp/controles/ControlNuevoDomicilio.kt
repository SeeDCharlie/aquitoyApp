package com.soportec.aquitoyapp.controles

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import com.soportec.aquitoyapp.vistas.NavegacionActivity
import org.json.JSONObject

class ControlNuevoDomicilio(var context: Context?, var fragment: Fragment): apiInterfaz{

    override var baseUrl: String = VariablesConf.BASE_URL_API
        set(value) {}
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)


    fun registrarDomicilio(dialog: Dialog){
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnok = dialog.findViewById<Button>(R.id.btnDiCoOk)
        val btncancel = dialog.findViewById<Button>(R.id.btnDiCoCancel)
        //eventos dialog
        btnok.setOnClickListener{
            registrarDomicilio()
            dialog.dismiss()
        }
        btncancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }


    fun registrarDomicilio(){
        //datos del nuevo domicilio
        var datos_domicilio = JSONObject()
        datos_domicilio.put("origen",NavegacionActivity.modNuevoDom.origen)
        datos_domicilio.put("destino",NavegacionActivity.modNuevoDom.destino)
        datos_domicilio.put("descripcion",NavegacionActivity.modNuevoDom.descripcion)
        datos_domicilio.put("id_cliente",NavegacionActivity.modNuevoDom.id_cliente)
        datos_domicilio.put("notas",NavegacionActivity.modNuevoDom.notas)
        //datos de le peticion post
        val datos = JSONObject()
        datos.put("nuevo_domicilio", true)
        datos.put("documento", NavegacionActivity.datosUsuario!!.getString("usu_documento"))
        datos.put("id_user", NavegacionActivity.datosUsuario!!.getInt("usu_id"))
        datos.put("contrasena", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
        datos.put("datos_domicilio", datos_domicilio)
        //peticion al servidor
        if(datos_domicilio.getString("origen") != "" && datos_domicilio.getString("destino") != ""
            && datos_domicilio.getInt("id_cliente") != -1){
            peticionPost(datos, "nuevoDomicilio.php")
        }else{
            Snackbar.make(fragment.requireView(), "Hay Campos Vacios", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun actionPost(obj: JSONObject) {
        super.actionPost(obj)
        NavegacionActivity.domicilioAux = obj.getJSONObject("datos_domicilio")
        Snackbar.make(fragment.requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        fragment.findNavController().navigate(R.id.action_nuevoDomicilioFrag_to_tomarDomicilioFrag)
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
