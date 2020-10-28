package com.example.aquitoyapp.modelos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.aquitoyapp.R


class rowAdapterDomDisp(context: Context, var resources: Int, var items: List<DomDisponible>) :
    ArrayAdapter<DomDisponible>(context, resources, items) {
    var contexts = context
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(contexts)
        val view = layoutInflater.inflate(resources, null)

        val txtOrigen: TextView = view.findViewById(R.id.textViewUno)
        val txtDestino: TextView = view.findViewById(R.id.tvRowDos)
        val txtEstado: TextView = view.findViewById(R.id.textViewTres)

        var myItem: DomDisponible = items[position]
        txtDestino.text = txtDestino.text.toString() + myItem.destino
        txtOrigen.text = txtOrigen.text.toString() + myItem.origen
        txtEstado.text = txtEstado.text.toString() + myItem.estado

        return view
    }

}


