package com.soportec.aquitoyapp.vistas

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlNuevoDomicilio
import com.soportec.aquitoyapp.modelos.NuevoDomicilio
import org.json.JSONObject


class NuevoDomicilioFrag : Fragment() {

    var controlFrag : ControlNuevoDomicilio? = null
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_domicilio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        //creacion de eventos

        //boton nuevo cliente
        /*view.findViewById<Button>(R.id.btnNuDoUno).setOnClickListener {
            findNavController().navigate(R.id.action_nuevoDomicilioFrag_to_getClienteFrg)
        }*/
        //boton cliente existente

        view.findViewById<Button>(R.id.btnNuDoDos).setOnClickListener {
            NavegacionActivity.switchGetCliente = 1
            catchDatosDomicilio()
            findNavController().navigate(R.id.action_nuevoDomicilioFrag_to_getClienteFrg)
        }
        // boton direccion origen
        view.findViewById<Button>(R.id.btnNuDoOrigen).setOnClickListener {
            NavegacionActivity.switchGetCliente = 2
            catchDatosDomicilio()
            findNavController().navigate(R.id.action_nuevoDomicilioFrag_to_getClienteFrg)
        }

        //boton direccion destino

        view.findViewById<Button>(R.id.btnNuDoDestino).setOnClickListener {
            NavegacionActivity.switchGetCliente = 3
            catchDatosDomicilio()
            findNavController().navigate(R.id.action_nuevoDomicilioFrag_to_getClienteFrg)
        }
        //boton registrar domicilio
        view.findViewById<Button>(R.id.btnNuDoTres).setOnClickListener{
            catchDatosDomicilio()
            controlFrag!!.registrarDomicilio(dialog)
        }

    }

    fun initView(v: View){
        var sw = NavegacionActivity.switchGetCliente
        controlFrag = ControlNuevoDomicilio(v.context, this)
        dialog = Dialog(v.context)

        if(sw > 0 && sw < 4){
            setDatosDomicilio()
        }
    }

    //recoge todos los datos del formulario y los retorna
    fun catchDatosDomicilio() {
        NavegacionActivity.modNuevoDom = NuevoDomicilio(
            NavegacionActivity.modNuevoDom.id_cliente,
            view?.findViewById<TextView>(R.id.txtNuDoTres)?.text!!.toString(),
            view?.findViewById<TextView>(R.id.txtNuDoOrigen)?.text!!.toString(),
            view?.findViewById<TextView>(R.id.txtNuDoOrigen)?.text!!.toString(),
            view?.findViewById<TextView>(R.id.edtNuDoTres)?.text!!.toString(),
            view?.findViewById<TextView>(R.id.edtNudoCuatro)?.text!!.toString()
        )
    }
    //intriduce todos los datos o compos al formulario
    //los datos viejedel objeto json datosDomicilio que se tranmite entre vistas
    fun setDatosDomicilio() {
        view?.findViewById<TextView>(R.id.txtNuDoTres)!!.text =
            NavegacionActivity.modNuevoDom.nombre_cliente
        view?.findViewById<TextView>(R.id.txtNuDoOrigen)!!.text = NavegacionActivity.modNuevoDom.origen
        view?.findViewById<TextView>(R.id.txtNuDoDestino)!!.text = NavegacionActivity.modNuevoDom.destino
        view?.findViewById<TextView>(R.id.edtNuDoTres)!!.text = NavegacionActivity.modNuevoDom.descripcion
        view?.findViewById<TextView>(R.id.edtNudoCuatro)!!.text = NavegacionActivity.modNuevoDom.notas
    }

}