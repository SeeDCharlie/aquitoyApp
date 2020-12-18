package com.soportec.aquitoyapp.controles

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject

class UpdateSqliteManager(
    context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams), apiInterfaz {

    var context = context
    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(this.context)

    var controlSqlite: ControlSql = ControlSql(this.context)


    fun updateDb(){
        var query = "select id from update_sqlite order by id desc;"
        println("proceso segundo plano de actualizacion!!")
        try {
            var id_up = controlSqlite.select(query)
            if (id_up != null && id_up!!.size > 0){
                println("solicitando actualizacion")
                var datos = JSONObject()
                datos.put("update_app", true)
                datos.put("id_up", id_up[0].getInt("id"))
                peticionPost(datos, "updateApp.php")
            }else{
                println("no es necesario actualizar la db")
            }
        }catch (error: Exception){
            var datos = JSONObject()
            datos.put("update_app", true)
            datos.put("id_up", -1)
            println("solicitando actualizacion dos  !!!")
            peticionPost(datos, "updateApp.php")
        }

    }

    override fun doWork(): Result {
        try {
            updateDb()
        }catch (error:Exception){
            print(error.message)
            return Result.retry()
        }
        return Result.success()
    }

    override fun actionPost(obj: JSONObject) {
        super.actionPost(obj)

        when (obj.getString("tag")){

            "getDbTables" -> {
                // se crea la base datos local
                var inserts = obj.getJSONObject("inserts")
                controlSqlite.motor_db.sqlTables = obj.getString("dbTables")
                controlSqlite.motor_db.sqlTableNames = controlSqlite.getTableNames()
                controlSqlite.motor_db.onUpgrade(controlSqlite.motor_db.readableDatabase, 1,1)
                controlSqlite.insertsVals(inserts, "var_config")
                println("se actualizo la db local !!")

            }
        }
    }

}