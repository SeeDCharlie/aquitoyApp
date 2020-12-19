package com.soportec.aquitoyapp.modelos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soportec.aquitoyapp.R
import kotlinx.android.synthetic.main.item_image_list.view.*
import java.io.ByteArrayOutputStream

class itemAdapterImageList (var items: ArrayList<modelImgEviden>?, val event: evtListEvid) :
RecyclerView.Adapter<itemAdapterImageList.ViewHolder>() {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(items!!.get(position))
        holder.itemView.setOnClickListener {
            event.onCLickListEvidDest(position ,items!! , it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layo = LayoutInflater.from(parent.context)
        return ViewHolder(layo.inflate(R.layout.item_image_list, parent, false))
    }

    override fun getItemCount(): Int = items!!.size

    class ViewHolder(val vista: View) : RecyclerView.ViewHolder(vista) {

        fun render(it: modelImgEviden) {
            vista.imgItem.setImageBitmap(it.img)
        }
    }

}