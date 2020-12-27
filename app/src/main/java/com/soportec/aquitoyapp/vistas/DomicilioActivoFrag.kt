package com.soportec.aquitoyapp.vistas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat.RESULT_OK
import androidx.recyclerview.widget.RecyclerView
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlDomicilioActivo
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.evtListEvid
import com.soportec.aquitoyapp.modelos.modelImgEviden
import java.util.*
import kotlin.concurrent.fixedRateTimer

class DomicilioActivoFrag : Fragment(), evtListEvid {


    var datosDomicilio = NavegacionActivity.domicilioAux
    var controlFrag : ControlDomicilioActivo? = null
    lateinit var dialog: Dialog;
    var code:Int = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_domicilio_activo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)


        //eventos

        //evento para agregar evidecias de origen
        view.findViewById<ImageButton>(R.id.btnDoAcAdduno).setOnClickListener {
            code = 1
            if (getPermissions(view)){
                val btnShet = btnStTakePhoto(this.controlFrag!!, code)
                btnShet.show(this.parentFragmentManager, "Aquitoy Msj")
            }else{
                Toast.makeText(this.context, "debe aceptar los permisos", Toast.LENGTH_SHORT).show()
            }
            //getFoto(view)
        }
        //evento para agregar evidecias de destino
        view.findViewById<ImageButton>(R.id.btnDoAcAdddos ).setOnClickListener {
            code = 2
            //VariablesConf.getPermissions(view)
            if(getPermissions(view)){
                val btnShet = btnStTakePhoto(this.controlFrag!!, code)
                btnShet.show(this.parentFragmentManager, "Aquitoy Msj")
            }else{
                Toast.makeText(this.context, "debe aceptar los permisos", Toast.LENGTH_SHORT).show()
            }
            //getFoto(view)
        }
        //evento para añadir notas al domicilio
        view.findViewById<ImageButton>(R.id.btnDoAcAddNote ).setOnClickListener {
            controlFrag!!.agregarNota(dialog)
        }

        //Evento confirmacion domicilio
        view.findViewById<Button>(R.id.btnDoAcOk).setOnClickListener {
            controlFrag!!.terminarDomicilio(dialog)
        }
        // menu
        view.findViewById<ImageButton>(R.id.btnDoAcMenu).setOnClickListener {
            controlFrag!!.popupMainMenu(it)
        }
    }

    fun initView(v:View){

        controlFrag = ControlDomicilioActivo(v.context ,this, this)
        dialog = Dialog(v.context)

        controlFrag!!.cargarFotos(NavegacionActivity.domicilioAux!!.getInt("dom_id"))

        var txtDesc = v.findViewById<TextView>(R.id.txtDoAcUno)
        var txtOrigen = v.findViewById<TextView>(R.id.txtDoAcDos)
        var txtDestino = v.findViewById<TextView>(R.id.txtDoAcTres)
        var txtCliente = v.findViewById<TextView>(R.id.txtDoAcCuatro)
        var txtFecha = v.findViewById<TextView>(R.id.txtDoAcCinco)
        var txtHora = v.findViewById<TextView>(R.id.txtDoAcSeis)

        txtDesc.text = txtDesc.text.toString() + datosDomicilio!!.getString("dom_descripcion")
        txtOrigen.text = txtOrigen.text.toString() + datosDomicilio!!.getString("dom_origen")
        txtDestino.text = txtDestino.text.toString() + datosDomicilio!!.getString("dom_destino")
        txtCliente.text = txtCliente.text.toString() + datosDomicilio!!.getString("cli_nombre")
        txtFecha.text = txtFecha.text.toString() + datosDomicilio!!.getString("dom_fechainicio")
        txtHora.text = txtHora.text.toString() + datosDomicilio!!.getString("dom_horainicio")

    }

    //funcion que pide permisos a los usuarios para utilizar la camara
    //y abrir la camara para luego recuperar la imagen tomada
    fun getPermissions(v:View):Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(v.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(v.context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(v.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED-> {
                    return true
                }
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), VariablesConf.PERMISSION_CAM_GALLER_CODE)
                    return true
                }
            }
        } else {
            return false
        }
    }

    //funcion que se ejecuta cuando el usuario a otorgado los permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            VariablesConf.PERMISSION_CAM_GALLER_CODE-> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //sí los permisos son concedidos
                    val btnShet = btnStTakePhoto(this.controlFrag!!, code)
                    btnShet.show(this.parentFragmentManager, "Aquitoy Msj")
                } else {
                    Toast.makeText(this.context, "Debe aceptar los permisos para continuar!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    //evento de las dos listas de imagenes de evidencias. el evento muestra la opcion de eliminar la evidencia
    override fun onCLickListEvidDest(pocicion: Int, lista: ArrayList<modelImgEviden>, v:View) {
        controlFrag!!.popupMenuEvid(pocicion, lista, v)
    }
}