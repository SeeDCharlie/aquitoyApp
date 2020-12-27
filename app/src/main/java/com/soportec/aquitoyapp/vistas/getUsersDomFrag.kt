package com.soportec.aquitoyapp.vistas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class getUsersDomFrag : Fragment(), apiInterfaz, eventRecyclerView {


    var usrsDomListObj: ArrayList<usrDom>? = null
    var usrsDomListObjAux: ArrayList<usrDom>? = null
    var searchv: SearchView? = null
    var adapter: itemAdampterUsrDom? = null
    var usrDomList: RecyclerView? = null

    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_users_dom, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)


        searchv!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0!!.isNotEmpty()) {
                    usrsDomListObjAux!!.clear()
                    usrsDomListObj!!.forEach {
                        // busca coincidencias en nombre y nombre comercial en tabla clientes
                        if (it.name.toLowerCase(Locale.ROOT)
                                .contains(p0.toLowerCase(Locale.ROOT)) or it.name.toLowerCase(
                                Locale.ROOT
                            )
                                .contains(p0.toLowerCase(Locale.ROOT))
                        ) {
                            usrsDomListObjAux!!.add(it)
                        }
                    }
                } else {
                    usrsDomListObjAux!!.clear()
                    usrsDomListObjAux!!.addAll(usrsDomListObj!!)
                }
                usrDomList!!.adapter!!.notifyDataSetChanged()
                return true
            }
        })
    }


    fun initView(v:View) {

        //datosDomicilio = JSONObject(intent.getStringExtra("datos_domicilio"))
        requestExecute = Volley.newRequestQueue(context)
        usrsDomListObj = ArrayList<usrDom>()
        usrsDomListObjAux = ArrayList<usrDom>()
        searchv = v.findViewById<SearchView>(R.id.schGetUsDoUno)
        usrDomList = v.findViewById(R.id.listGetUsDoUno)
        usrDomList?.layoutManager = LinearLayoutManager(context)

        val datos = JSONObject()
        datos.put("get_users_dom", true)
        datos.put("documento", NavegacionActivity.datosUsuario!!.getString("usu_documento"))
        datos.put("contrasena", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
        datos.put("lat", VariablesConf.USU_LATITUD)
        datos.put("long", VariablesConf.USU_LONGITUD)

        peticionPost(datos, "getUsersDomDis.php")

    }


    //evento de click en la lista de usuarios
    override fun onCLick(pocicion: Int) {
        passDom(pocicion)
    }


    fun passDom(pocicion:Int){
        var datos = JSONObject()
        datos.put("pass_dom", true)
        datos.put("documento", NavegacionActivity.datosUsuario!!.getString("usu_documento"))
        datos.put("contrasena", NavegacionActivity.datosUsuario!!.getString("usu_pass"))
        datos.put("id_user", usrsDomListObj!!.get(pocicion).id )
        datos.put("id_dom", NavegacionActivity.domicilioAux!!.getString("dom_id"))
        peticionPost(datos, "passDom.php")
    }

    fun createList(listUsr:JSONObject){
        listUsr.keys().forEach {
            var usr = listUsr.getJSONObject(it)
            this.usrsDomListObj!!.add(
                usrDom(
                    usr.getInt("usu_id"),
                    usr.getString("usu_nombre"),
                    usr.getString("usu_apellidos"),
                    usr.getInt("no_dom"),
                    usr.getDouble("distance")
                )
            )
        }
        usrsDomListObjAux!!.addAll(this.usrsDomListObj!!)
        adapter = itemAdampterUsrDom(this.usrsDomListObjAux!!, this)
        usrDomList?.adapter = adapter
    }



    // funcion que se activa cuando se recibe la respuesta del servidor
    override fun actionPost(obj: JSONObject) {
        super.actionPost(obj)
        when(obj.getString("tag")){
            "usersDom" -> {
                createList(obj.getJSONObject("usersDats"))
            }
            "pass_dom" -> {
                Snackbar.make(requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                findNavController().navigate(R.id.action_getUsersDom_to_domiciliosAvtivosFrag)
            }

        }


    }

    override fun errorOk(obj: JSONObject) {
        super.errorOk(obj)
        Snackbar.make(requireView(), obj.getString("msj"), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }

    override fun errorRequest(msj: String) {
        super.errorRequest(msj)
        Snackbar.make(requireView(), msj, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }

}