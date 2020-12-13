package com.soportec.aquitoyapp.modelos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class DbLite(context: Context, sql: String = "") :  SQLiteOpenHelper(context, "aqitoyDb", null, 1){

    var sqlTables :String = sql

    override fun onCreate(db: SQLiteDatabase?) {

        if (sqlTables != ""){

            sqlTables.split(";").forEach {
                try {
                    db?.execSQL("$it;")
                    println(it)
                }catch (e : Exception){
                    println("erro al crear la db ${e.toString()} + ${e.message} + ${e.cause}")
                }
            }
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists sesiones;")
        db.execSQL("drop table if exists urievidencias;")
        onCreate(db)
    }


}
