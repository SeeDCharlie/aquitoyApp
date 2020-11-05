package com.example.aquitoyapp.modelos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aquitoyapp.R
import kotlinx.android.synthetic.main.row_dos.view.*

class rowAdapterClientes(items: ArrayList<Cliente>) :
    RecyclerView.Adapter<rowAdapterClientes.ViewHolder>() {

    var items: ArrayList<Cliente>? = null
    var viewHolder: ViewHolder? = null

    init {
        this.items = items
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.id_cliente?.text = item?.id_cliente.toString()
        holder.nombre?.text = item?.nombre
        holder.telefono?.text = item?.telefono
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.row_dos, parent, false)
        viewHolder = ViewHolder(vista)
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        var vista = vista
        var id_cliente: TextView? = vista.txtRowDosUno
        var nombre: TextView? = vista.txtRowDosDos
        var telefono: TextView? = vista.txtRowDosTres
    }
}

