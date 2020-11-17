package com.soportec.aquitoyapp.modelos

import java.io.Serializable

class Sesiones : Serializable {
    var _id = -1
    var id_user = -1
    var email = ""
    var nombres = ""
    var apellidos = ""
    var documento = ""
    var contraseña = ""
    var fecha_creacion = ""
    var activo = 0

}