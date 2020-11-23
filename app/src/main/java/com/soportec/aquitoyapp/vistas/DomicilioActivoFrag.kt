package com.soportec.aquitoyapp.vistas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlDomicilioActivo
import com.soportec.aquitoyapp.modelos.VariablesConf
import java.util.*
import kotlin.concurrent.fixedRateTimer

class DomicilioActivoFrag : Fragment() {


    var datosDomicilio = NavegacionActivity.domicilioAux
    var controlFrag : ControlDomicilioActivo? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
            getFoto(1,view)
        }
        //evento para agregar evidecias de destino
        view.findViewById<ImageButton>(R.id.btnDoAcAdddos ).setOnClickListener {
            getFoto(0,view)
        }
        //evento para añadir notas al domicilio
        view.findViewById<ImageButton>(R.id.btnDoAcAddNote ).setOnClickListener {

        }

        //Evento confirmacion domicilio
        view.findViewById<ImageButton>(R.id.btnDoAcOk).setOnClickListener {

        }
        //evento cancelar domicilio
        view.findViewById<ImageButton>(R.id.btnDoAcCancel).setOnClickListener {

        }
    }

    fun initView(v:View){

        controlFrag = ControlDomicilioActivo(v.context ,this)

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
    //y abrir la camara para luegorecuperar la imagen tomada
    fun getFoto(code:Int, v:View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*if (ContextCompat.checkSelfPermission(v.context,Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission( v.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                ActivityCompat.requestPermissions(requireActivity(), permission)
            } else {
                //permission already granted
                controlFrag!!.abrirCamara(code)
            }*/
            when {
                ContextCompat.checkSelfPermission(v.context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(v.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED-> {
                    // You can use the API that requires the permission.
                    controlFrag!!.abrirCamara(code)
                }

                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissions(arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE), targetRequestCode)
                }
            }
        } else {
            //system os is < marshmallow
            controlFrag!!.abrirCamara(code)
        }
    }


    //funcion que pide permisos para acceder a la galeria de imagenes
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            VariablesConf.PERMISSION_CAM_CODE-> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    controlFrag!!.captureImg()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    //oyente que captura la imagen seleccionada de la camara
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK /*&& requestCode == IMAGE_PICK_CODE*/) {
            controlFrag?.captureImg()
        }
    }

}