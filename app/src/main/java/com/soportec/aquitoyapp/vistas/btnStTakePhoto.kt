package com.soportec.aquitoyapp.vistas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soportec.aquitoyapp.R

class btnStTakePhoto: BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.option_cam_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.btnOpCaGa).setOnClickListener {
            Toast.makeText(context, "Btn galeria", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageButton>(R.id.btnOpCaFo).setOnClickListener {
            Toast.makeText(context, "Btn tomar foto", Toast.LENGTH_SHORT).show()
        }
    }
}