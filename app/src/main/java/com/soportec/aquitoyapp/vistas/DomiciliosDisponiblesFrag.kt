package com.soportec.aquitoyapp.vistas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlDomiciliosDisponibles


class DomiciliosDisponiblesFrag : Fragment() {

    var controlFrag: ControlDomiciliosDisponibles? = null

    companion object {
        fun newInstance() = DomiciliosDisponiblesFrag()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_domicilios_disponibles, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ListView>(R.id.listViewUno)
            ?.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                //llamo a la vista tomar_domicilio
                NavegacionActivity.domicilioAux = controlFrag!!.domicilios_disponibles!!.getJSONObject(position.toString())
                findNavController().navigate(R.id.disponibles_tomar_trans )
            }

        initView(view)
    }

    fun initView(view:View) {
        controlFrag = ControlDomiciliosDisponibles(view.context,this)
        controlFrag!!.cargarDomicilios()
    }


}