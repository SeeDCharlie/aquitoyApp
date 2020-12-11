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



    override fun onCreate(db: SQLiteDatabase?) {

        /*VariablesConf.sqlTables.split(";").forEach {
            try {
                db?.execSQL(it + ";")
                println(it)
            }catch (e : Exception){
                println("erro al crear la db ${e.toString()} + ${e.message} + ${e.cause}")
            }
        }*/

        db?.execSQL("create table if not exists  sesiones(\n" +
                "id                integer primary key AUTOINCREMENT,\n" +
                "id_user           integer,\n" +
                "email             text,\n" +
                "nombres           text,\n" +
                "apellidos         text,\n" +
                "documento         text,\n" +
                "contrasena        text,\n" +
                "fecha_creacion    text,\n" +
                "activo            integer default 0);")
        db?.execSQL("create table if not exists urievidencias(\n" +
                "id      integer primary key AUTOINCREMENT,\n" +
                "id_dom  integer,\n" +
                "uri     text,\n" +
                "origen_destino integer);")
        db?.execSQL("create table if not exists var_config(\n" +
                "id              integer primary key AUTOINCREMENT,\n" +
                "valor           text);")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists sesiones;")
        db.execSQL("drop table if exists urievidencias;")
        onCreate(db)
    }


}
