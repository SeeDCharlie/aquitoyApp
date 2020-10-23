package com.example.aquitoyapp.modelos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.BufferedReader
import java.io.File
import java.lang.Exception


class DbLite ( context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {



    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(getSqlTables())
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists Sesiones;")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "userSession.db"
    }
        //query para la creacion de la tabla en la base de datos sqlite

    fun getSqlTables(): String {

        return "create table if not exists  sesiones(\n" +
                "    id                integer primary key AUTOINCREMENT,\n" +
                "    email             TEXT,\n" +
                "    nombre_usuario    text,\n" +
                "    contraseña        text,\n" +
                "    fecha_creacion    text,\n" +
                "    activo            integer default 0)"
        }

        //devuelve la base de datos lista para editar o para ingresar datos

    fun getDb():SQLiteDatabase{
        return this.writableDatabase
    }


}
