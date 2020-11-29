package com.soportec.aquitoyapp.modelos

class NuevoDomicilio(
    private var id_cliente:Int?= -1,
    private var nombre_cliente:String? = "",
    private var origen:String? = "",
    private var destino:String? = "",
    private var descripcion:String? = "",
    private var notas:String? = ""
){
    //getters
    fun getId_cliente():Int?{
        return id_cliente
    }
    fun getNombre_cliente():String?{
        return nombre_cliente
    }
    fun getOrigen():String?{
        return origen
    }
    fun getDestino():String?{
        return destino
    }
    fun getDescripcion():String?{
        return descripcion
    }
    fun getNotas():String?{
        return notas
    }

    //setters
    fun setId_cliente(dat:Int?){
        id_cliente = dat
    }

    fun setNombre_cliente(dat:String?){
        nombre_cliente = dat
    }
    fun setOrigen(dat:String?){
        origen = dat
    }
    fun setDestino(dat:String?){
        destino = dat
    }
    fun setDescripcion(dat:String?){
        descripcion = dat
    }
    fun setNotas(dat:String?){
        notas = dat
    }

    override fun toString(): String {
        return "NuevoDomicilio(id_cliente=$id_cliente, nombre_cliente=$nombre_cliente, origen=$origen, destino=$destino, descripcion=$descripcion, notas=$notas)"
    }
}
