package com.soportec.aquitoyapp.vistas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.soportec.aquitoyapp.R
import org.json.JSONObject


class NuevoDomicilioFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        initView()

        //creacion de eventos

        //boton nuevo cliente
        view.findViewById<Button>(R.id.btnNuDoUno).setOnClickListener {

        }
        //boton cliente existente

        view.findViewById<Button>(R.id.btnNuDoDos).setOnClickListener {

        }
        // boton direccion origen
        view.findViewById<Button>(R.id.btnNuDoOrigen).setOnClickListener {

        }

        //boton direccion destino

        view.findViewById<Button>(R.id.btnNuDoDestino).setOnClickListener {

        }
        //boton cancelar
        view.findViewById<Button>(R.id.btnNuDoCuatro).setOnClickListener {

        }
    }

    fun initView(){

    }

    //recoge todos los datos del formulario y los retorna
    fun getDatosDomicilio(): JSONObject {
        var dats = JSONObject()
        dats.put("nombre_cliente", view?.findViewById<TextView>(R.id.txtNuDoTres)!!.text.toString())
        dats.put("origen", view?.findViewById<TextView>(R.id.txtNuDoOrigen)!!.text.toString())
        dats.put("destino", view?.findViewById<TextView>(R.id.txtNuDoDestino)!!.text.toString())
        dats.put("descripcion", view?.findViewById<TextView>(R.id.edtNuDoTres)!!.text.toString())
        dats.put("notas", view?.findViewById<TextView>(R.id.edtNudoCuatro)!!.text.toString())
        dats.put("id_cliente", NavegacionActivity.domicilioAux!!.getInt("id_cliente"))
        return dats
    }

    //intriduce todos los datos o compos al formulario
    //los datos viejedel objeto json datosDomicilio que se tranmite entre vistas
    fun setDatosDomicilio() {
        /*view?.findViewById<TextView>(R.id.txtNuDoTres).text =
            datosDomicilios!!.getString("nombre_cliente")
        view?.findViewById<TextView>(R.id.txtNuDoOrigen).text = datosDomicilios!!.getString("origen")
        view?.findViewById<TextView>(R.id.txtNuDoDestino).text = datosDomicilios!!.getString("destino")
        view?.findViewById<TextView>(R.id.edtNuDoTres).text = datosDomicilios!!.getString("descripcion")
        view?.findViewById<TextView>(R.id.edtNudoCuatro).text = datosDomicilios!!.getString("notas")*/
    }

}