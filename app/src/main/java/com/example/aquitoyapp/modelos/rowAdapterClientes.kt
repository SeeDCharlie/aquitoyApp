package com.example.aquitoyapp.modelos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aquitoyapp.R
import kotlinx.android.synthetic.main.row_dos.view.*

class rowAdapterClientes(val items: ArrayList<Cliente>) :
    RecyclerView.Adapter<rowAdapterClientes.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(items.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layo = LayoutInflater.from(parent.context)
        return ViewHolder(layo.inflate(R.layout.row_dos, parent, false))
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val vista: View) : RecyclerView.ViewHolder(vista) {

        fun render(cliente: Cliente) {
            vista.txtRowDosUno.text = cliente.id_cliente.toString()
            vista.txtRowDosDos.text = cliente.nombre
            vista.txtRowDosTres.text = cliente.telefono
        }
    }
}

