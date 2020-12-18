package com.soportec.aquitoyapp.vistas

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject

class TomarDomicilioFrag : Fragment(), apiInterfaz {

    override var baseUrl: String =  VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = null
    var datosDomicilio: JSONObject? = null
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tomar_domicilio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        //evento del boton 'empezar' para empezar un domicilio
        view.findViewById<Button>(R.id.btnToDoUno).setOnClickListener {
            empezarDomicilio()

        }

    }
    //funcion que inicia un dialogo en la pantalla para pedir la confirmacion
    //paraempezar un domicilio
    fun empezarDomicilio(){
        //se el layout correspondiente al dialog
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //referenciamos los botones del dialog
        val btnok = dialog.findViewById<Button>(R.id.btnDiCoOk)
        val btncancel = dialog.findViewById<Button>(R.id.btnDiCoCancel)
        //eventos de los botonesde dialog
        btnok.setOnClickListener {
            var usu = NavegacionActivity.datosUsuario
            var datos = JSONObject()
            datos.put("tomar_domicilio", true)
            datos.put("id_user", usu!!.getString("usu_id"))
            datos.put("documento", usu!!.getString("usu_documento"))
            datos.put("contrasena", usu.getString("usu_pass"))
            datos.put("id_domicilio", datosDomicilio!!.getInt("dom_id"))

            peticionPost(datos, "empezarDomicilio.php")
            dialog.dismiss()
        }
        btncancel.setOnClickListener{
            dialog.dismiss()
        }
        //mostramos el dialog
        dialog.show()
    }


    fun initView(v: View) {
        datosDomicilio = NavegacionActivity.domicilioAux
        requestExecute = Volley.newRequestQueue(context)
        dialog = Dialog(v.context)

        val tvUno = v.findViewById<TextView>(R.id.tvToDoOrigen)
        val tvDos = v.findViewById<TextView>(R.id.tvToDoDestino)
        val tvTres = v.findViewById<TextView>(R.id.tvToDoDescripcion)
        val tvCuatro = v.findViewById<TextView>(R.id.tvToDoFechaAsig)
        val tvCinco = v.findViewById<TextView>(R.id.tvToDoCliente)
        val tvSeis = v.findViewById<TextView>(R.id.tvToDoNotas)

        tvUno.text = tvUno.text.toString() + datosDomicilio!!.getString("dom_origen")
        tvDos.text = tvDos.text.toString() + datosDomicilio!!.getString("dom_destino")
        tvTres.text = tvTres.text.toString() + datosDomicilio!!.getString("dom_descripcion")
        tvCuatro.text = tvCuatro.text.toString() + datosDomicilio!!.getString("dom_fechaasignacion")
        tvCinco.text = tvCinco.text.toString() + datosDomicilio!!.getString("cli_nombre")
        tvSeis.text = tvSeis.text.toString() + datosDomicilio!!.getString("dom_notas")

    }


    //funciones heredadas de la interfaz apiInterfaz
    override fun actionPost(obj: JSONObject) {
        super.actionPost(obj)
        Toast.makeText(context, obj.getString("msj"), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.tomar_dom_activo_trans )
    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Toast.makeText(view?.context, obj.getString("msj"), Toast.LENGTH_SHORT).show()
    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Toast.makeText(view?.context, msj, Toast.LENGTH_SHORT).show()
    }


}