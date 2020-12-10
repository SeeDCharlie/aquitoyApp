package com.soportec.aquitoyapp.modelos

import android.content.ContentProviderOperation.newCall
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.io.Serializable


class DbLite(context: Context) :  SQLiteOpenHelper(context, "aqitoyDb", null, 1) {

    var sqlTables: String = ""

    override fun onCreate(db: SQLiteDatabase?) {

        val request = Request.Builder()
            .url(VariablesConf.BASE_URL_API + "FilesConfig/db_sqlite_app.sql")
            .build()
        val client: OkHttpClient = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    sqlTables = response.body!!.string()

                }
            }
        })

        println("antes del tread !!")
        Thread.sleep(5000)

        sqlTables.split(";").forEach {
            try {
                db?.execSQL(it + ";")
                println(it)
            }catch (e : Exception){
                println("erro al crear la db ${e.toString()} + ${e.message} + ${e.cause}")
            }

        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists sesiones;")
        db.execSQL("drop table if exists urievidencias;")
        onCreate(db)
    }

}
