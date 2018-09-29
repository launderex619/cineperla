package com.karaokulta.test.cineperla;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.SharedPreferences;


/**
 * Created by carlo on 02/05/2018.
 */

class DataBaseCommands{
    public static String DATABASE_NAME = "DBProyecto";
    public static int DATABASE_VERSION = 1;


    //tabla pelicula:
    public static String CREATE_TABLE_PELICULA = "create table IF NOT EXISTS pelicula(" +
            "codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            "nombre text NOT NULL UNIQUE, " +
            "genero text NOT NULL, " +
            "director text NOT NULL, " +
            "clasificacion text NOT NULL, " +
            "actores text NOT NULL, " +
            "duracion integer NOT NULL, "+
            "filePath text NOT NULL,"+
            "sipnosis text NOT NULL)";

    //tabla funcion
    public static String CREATE_TABLE_FUNCION = "create table IF NOT EXISTS funcion(" +
            "codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            "fk_pelicula text NOT NULL, " +
            "sala integer NOT NULL, " +
            "asientos text NOT NULL, " +
            "boletos integer NOT NULL, " +
            "hora datetime NOT NULL)";

    //tabla boletos
    public static String CREATE_TABLE_BOLETO = "create table IF NOT EXISTS boleto(" +
            "codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            "fk_funcion INTEGER NOT NULL, " +
            "fk_usuario INTEGER NOT NULL, " +
            "cantidad_boletos INTEGER NOT NULL, " +
            "compra_en_linea INTEGER NOT NULL, " +   //1 TRUE, 0 FALSE
            "precio_total text NOT NULL)";

    //tabla usuarios
    public static String CREATE_TABLE_USUARIO = "create table IF NOT EXISTS usuario(" +
            "codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            "nombre text NOT NULL, " +
            "correo text NOT NULL UNIQUE, " +
            "contrasena text NOT NULL, " +
            "puntos INTEGER NOT NULL, " +
            "edad INTEGER NOT NULL, " +
            "credito INTEGER NOT NULL, "+
            "premium INTEGER NOT NULL)";//1 TRUE, 0 FALSE

    //table empleado

    public static String CREATE_TABLE_EMPLEADO = "create table IF NOT EXISTS empleado(" +
            "codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            "nombre text NOT NULL, " +
            "nss text NOT NULL, " +
            "fecha_nacimiento DATE NOT NULL, " +
            "direccion text NOT NULL, " +
            "telefono text NOT NULL, " +
            "contrasena text NOT NULL, " +
            "email text NOT NULL, " +
            "fk_puesto INTEGER NOT NULL)";

    //puesto
    public static String CREATE_TABLE_PUESTO = "create table IF NOT EXISTS puesto(" +
            "codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            "nombre_puesto text NOT NULL, " +
            "salario INTEGER NOT NULL)";
}


public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //aquí creamos la tabla de peliculas (codigo, nombre, genero, director, clasificacion, actores, duracion)
        db.execSQL(DataBaseCommands.CREATE_TABLE_PELICULA);
        db.execSQL(DataBaseCommands.CREATE_TABLE_BOLETO);
        db.execSQL(DataBaseCommands.CREATE_TABLE_FUNCION);
        db.execSQL(DataBaseCommands.CREATE_TABLE_USUARIO);
        db.execSQL(DataBaseCommands.CREATE_TABLE_EMPLEADO);
        db.execSQL(DataBaseCommands.CREATE_TABLE_PUESTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

