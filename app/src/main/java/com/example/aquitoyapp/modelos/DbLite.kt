package com.example.aquitoyapp.modelos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable


class DbLite(context: Context) : Serializable, SQLiteOpenHelper(context, "userSessionDb", null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(getSqlTables())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists sesiones;")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "userSessionDb"
    }
        //query para la creacion de la tabla en la base de datos sqlite

    fun getSqlTables(): String {

        return "create table if not exists  sesiones(\n" +
                "    id                integer primary key AUTOINCREMENT,\n" +
                "    id_user           integer,\n" +
                "    email             TEXT,\n" +
                "    nombres           text,\n" +
                "    apellidos         text\n" +
                "    documento         text\n" +
                "    contraseña        text,\n" +
                "    fecha_creacion    text,\n" +
                "    activo            integer default 0);"
        }

        //devuelve la base de datos lista para editar o para ingresar datos

    fun getDb():SQLiteDatabase{
        return this.writableDatabase
    }


}
