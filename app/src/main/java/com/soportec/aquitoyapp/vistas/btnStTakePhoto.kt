package com.soportec.aquitoyapp.vistas

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlDomicilioActivo
import com.soportec.aquitoyapp.modelos.VariablesConf

class btnStTakePhoto(control:ControlDomicilioActivo, code:Int): BottomSheetDialogFragment() {

    var control: ControlDomicilioActivo = control
    var code:Int = code

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.option_cam_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        control.switchCamara = code

        view.findViewById<ImageButton>(R.id.btnOpCaGa).setOnClickListener {
            openGallery()
        }
        view.findViewById<ImageButton>(R.id.btnOpCaFo).setOnClickListener {
            openCamera()
        }

    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, VariablesConf.IMAGE_PICK_GALLERY_CODE)
    }


    fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        control.image_uri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, control.image_uri)
        startActivityForResult(cameraIntent, VariablesConf.IMAGE_PICK_CAM_CODE)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK ){
            when (requestCode){
                VariablesConf.IMAGE_PICK_CAM_CODE -> {
                    control.captureImg(code)
                    dismiss()
                }
                VariablesConf.IMAGE_PICK_GALLERY_CODE->{
                    control.image_uri = data?.data
                    control.captureImg(code)
                    dismiss()
                }
            }
        }
    }


}