package com.soportec.aquitoyapp.modelos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soportec.aquitoyapp.R
import kotlinx.android.synthetic.main.user_dom_model_row.view.*

class itemAdampterUsrDom ( var items: ArrayList<usrDom>?, val event: eventRecyclerView) :
    RecyclerView.Adapter<itemAdampterUsrDom.ViewHolder>() {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(items!!.get(position))
        holder.itemView.setOnClickListener {
            event.onCLick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layo = LayoutInflater.from(parent.context)
        return ViewHolder(layo.inflate(R.layout.user_dom_model_row, parent, false))
    }

    override fun getItemCount(): Int = items!!.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun render(it: usrDom) {

            view.txtGetUsrDomNames.text = (it.name + " " + it.lasName)
            view.txtGetUsrDoNo_dom.text = (it.no_dom.toString())
            view.txtGetUsrDoUbi.text = (it.distamce.toString())
        }
    }


}