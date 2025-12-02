package com.example.proyectofinalaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
public class Base extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "agenda_escolar.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    public static final String TABLE_TAREAS = "tareas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_MATERIA = "materia";
    public static final String COLUMN_PRIORIDAD = "prioridad";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_COMPLETADA = "completada";

    public Base(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Aquí creamos la tabla cuando la App se instala por primera vez
        // SQL: CREATE TABLE tareas (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, ...)
        String query = "CREATE TABLE " + TABLE_TAREAS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                COLUMN_MATERIA + " TEXT, " +
                COLUMN_PRIORIDAD + " TEXT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_COMPLETADA + " INTEGER)"; // Guardamos boolean como 0 o 1
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si actualizamos la App y cambiamos la estructura, esto resetea la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREAS);
        onCreate(db);
    }

    // --- MÉTODOS CRUD (Create, Read, Update, Delete) ---

    // 1. AGREGAR TAREA (Alta)
    public long agregarTarea(Tarea tarea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITULO, tarea.getTitulo());
        values.put(COLUMN_DESCRIPCION, tarea.getDescripcion());
        values.put(COLUMN_MATERIA, tarea.getMateria());
        values.put(COLUMN_PRIORIDAD, tarea.getPrioridad());
        values.put(COLUMN_FECHA, tarea.getFecha());
        values.put(COLUMN_COMPLETADA, tarea.isCompletada() ? 1 : 0); // Convertir bool a int

        long id = db.insert(TABLE_TAREAS, null, values);
        db.close();
        return id;
    }

    // 2. OBTENER TODAS LAS TAREAS (Consulta para el ListView/RecyclerView)
    public List<Tarea> obtenerTodasLasTareas() {
        List<Tarea> listaTareas = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TAREAS; // Trae todo
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Sacamos los datos de las columnas
                int id = cursor.getInt(0);
                String titulo = cursor.getString(1);
                String descripcion = cursor.getString(2);
                String materia = cursor.getString(3);
                String prioridad = cursor.getString(4);
                String fecha = cursor.getString(5);
                boolean completada = cursor.getInt(6) == 1; // Convertir int a bool

                Tarea tarea = new Tarea(id, titulo, descripcion, materia, prioridad, fecha, completada);
                listaTareas.add(tarea);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTareas;
    }

    // 3. ACTUALIZAR TAREA (Cambio/Modificación)
    public int actualizarTarea(Tarea tarea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITULO, tarea.getTitulo());
        values.put(COLUMN_DESCRIPCION, tarea.getDescripcion());
        values.put(COLUMN_MATERIA, tarea.getMateria());
        values.put(COLUMN_PRIORIDAD, tarea.getPrioridad());
        values.put(COLUMN_FECHA, tarea.getFecha());
        values.put(COLUMN_COMPLETADA, tarea.isCompletada() ? 1 : 0);

        // Actualiza donde el ID sea igual al ID de la tarea
        return db.update(TABLE_TAREAS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(tarea.getId())});
    }

    // 4. ELIMINAR TAREA (Baja)
    public void eliminarTarea(int idTarea) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAREAS, COLUMN_ID + " = ?", new String[]{String.valueOf(idTarea)});
        db.close();
    }
}