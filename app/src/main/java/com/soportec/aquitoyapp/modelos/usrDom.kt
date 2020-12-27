package com.soportec.aquitoyapp.modelos

class usrDom (val id:Int, val name: String, val lasName: String, val no_dom: Int,  distamce:Double){


    var distamce: String

    init {
        if (distamce == -1.00){
            this.distamce = "No disponible"
        }else{
            this.distamce = String.format("%.3f", distamce)
        }
    }
}