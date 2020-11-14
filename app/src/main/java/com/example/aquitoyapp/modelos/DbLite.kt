package com.example.aquitoyapp.modelos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable


class DbLite(context: Context) : Serializable, SQLiteOpenHelper(context, "userSessionDb", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "create table if not exists  sesiones(\n" +
                    "id                integer primary key AUTOINCREMENT,\n" +
                    "    id_user           integer,\n" +
                    "    email             text,\n" +
                    "    nombres           text,\n" +
                    "    apellidos         text,\n" +
                    "    documento         text,\n" +
                    "    contraseña        text,\n" +
                    "    fecha_creacion    text,\n" +
                    "    activo            integer default 0);"
        )
        db?.execSQL(
            "create table if not exists urievidencias(\n" +
                    "    id      integer primary key AUTOINCREMENT,\n" +
                    "    id_dom  integer,\n" +
                    "    uri     text,\n" +
                    "    origen_destino integer); \n"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists sesiones;")
        onCreate(db)
    }

}
