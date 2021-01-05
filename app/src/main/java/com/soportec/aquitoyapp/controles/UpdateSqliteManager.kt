package com.soportec.aquitoyapp.controles

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject
import kotlin.concurrent.thread

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
        var datos = JSONObject()
        var id_up = controlSqlite.select(query)
        datos.put("update_app", true)
        if (id_up != null && id_up!!.size > 0){
            datos.put("id_up", id_up[0].getInt("id"))
        }else{
            datos.put("id_up", -1)
        }
        peticionPost(datos, "updateApp.php")
        println("solicitando actualizacion")

    }

    override fun doWork(): Result {
        try {
            updateDb()
            println("se ejecuto la actualizacion de la db ")
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
                var datsUpdate = obj.getJSONObject("updateID")
                var inserts = obj.getJSONObject("inserts")
                val valuesUpdate = ContentValues().apply {
                    put("id" , datsUpdate.getInt("id"))
                    put("update_date" , datsUpdate.getString("update_date"))
                    put("update_hour" , datsUpdate.getString("update_hour"))
                }
                controlSqlite.motor_db.sqlTables = obj.getString("dbTables")
                controlSqlite.motor_db.sqlTableNames = arrayListOf("sesiones", "urievidencias", "var_config", "update_sqlite")
                println("table names : ${controlSqlite.getTableNames().size}")
                controlSqlite.motor_db.onUpgrade(controlSqlite.motor_db.writableDatabase, 1,1)
                controlSqlite.insertsVals(inserts, "var_config")
                controlSqlite.insert(valuesUpdate, "update_sqlite")
                println("*************************************\n" + "se actualizo la db local !!")

            }
            else -> println("******************\n" + "no se actualizo la db o no es necesario")
        }
    }


}