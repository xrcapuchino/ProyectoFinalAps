package com.example.proyectofinalaps;

public class Tarea {
    // Estos son los campos que vimos en el pizarrón
    private int id;
    private String titulo;
    private String descripcion;
    private String materia;   // Para el Spinner (Combo)
    private String prioridad; // Para el Radio Button
    private String fecha;     // Fecha de entrega
    private boolean completada; // Para el Checkbox

    // Constructor vacío (necesario para algunas operaciones)
    public Tarea() {
    }

    // Constructor completo (para crear nuevas tareas fácil)
    public Tarea(int id, String titulo, String descripcion, String materia, String prioridad, String fecha, boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.materia = materia;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.completada = completada;
    }

    // Constructor sin ID (la base de datos pondrá el ID sola)
    public Tarea(String titulo, String descripcion, String materia, String prioridad, String fecha, boolean completada) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.materia = materia;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.completada = completada;
    }

    // Getters y Setters (para leer y escribir los datos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }
}