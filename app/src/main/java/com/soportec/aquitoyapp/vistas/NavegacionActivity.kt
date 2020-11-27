package com.soportec.aquitoyapp.vistas


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.controles.ControlSql
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import kotlinx.android.synthetic.main.activity_navegacion.*
import kotlinx.android.synthetic.main.activity_navegacion.view.*
import org.json.JSONObject

class NavegacionActivity : AppCompatActivity(),  apiInterfaz{

    private lateinit var appBarConfiguration: AppBarConfiguration
    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = null

    var controldblite: ControlSql? = null

    companion object{
        var datosUsuario : JSONObject? = null
        var domicilioAux : JSONObject? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegacion)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bundle = Bundle()
        bundle.putString("edttext", "From Activity")
        val fragobj = DomiciliosDisponiblesFrag()
        fragobj.setArguments(bundle)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Boton agregar Domicilio en proceso", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.domiciliosDisponiblesFrag,R.id.domiciliosAvtivosFrag,
                R.id.nuevoDomicilioFrag, R.id.logginActivity
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        initView(navView.getHeaderView(0) as View, navView, navController)
    }




    fun initView(view: View, nv: NavigationView, nc: NavController){

        //inicio de variables
        requestExecute = Volley.newRequestQueue(this)
        datosUsuario = JSONObject(intent.getStringExtra("datos_usuario"))
        controldblite = ControlSql(this)

        //asignacion de los datos del usuario al menu
        view.findViewById<TextView>(R.id.txtMePrUsername).setText(
            datosUsuario!!.getString("usu_nombre")
                    + datosUsuario!!.getString("usu_apellidos")
        )
        view.findViewById<TextView>(R.id.txtMePrTypeuser).setText(datosUsuario!!.getString("tipousu_nombre"))
        view.findViewById<TextView>(R.id.txtMePrEmail).setText(datosUsuario!!.getString("usu_correo"))

        //inicio de eventos del menu
        nv.setNavigationItemSelectedListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
            when(it.itemId){
                R.id.domiciliosDisponiblesFrag -> {
                    nc.navigate(R.id.domiciliosDisponiblesFrag)
                    Toast.makeText(this, "item action", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.domiciliosAvtivosFrag -> {
                    nc.navigate(R.id.domiciliosAvtivosFrag)
                    true
                }
                R.id.logginActivity -> {
                    logOut()

                    true
                }

                else ->{
                   true
                }
            }
        }



    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navegacion, menu)
        return true
    }
*/
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




    fun logOut(){
        var datos = JSONObject()
        datos.put("logout", true)
        datos.put("documento", datosUsuario!!.getString("usu_documento"))
        datos.put("contrasena", datosUsuario!!.getString("usu_pass"))
        respuestaPost(datos, "logOut.php")
    }


    override fun acionPots(obj: JSONObject) {
        var vista =  Intent(this, LogginActivity::class.java)
        val valores = ContentValues().apply { put("activo", 0) }
        controldblite!!.actualizarDato(
            "sesiones", valores, "documento = ?", arrayOf(datosUsuario!!.getString("usu_documento"))
        )
        Toast.makeText(this, obj.getString("msj"), Toast.LENGTH_SHORT).show()
        startActivity(vista)
        finish()
        super.onBackPressed()
    }

    override fun errorOk(obj: JSONObject) {
        Toast.makeText(this, obj.getString("msj"), Toast.LENGTH_SHORT).show()
    }

    override fun errorRequest(msj: String) {
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()
    }


}


