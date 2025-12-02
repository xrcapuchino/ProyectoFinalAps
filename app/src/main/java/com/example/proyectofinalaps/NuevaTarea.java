package com.example.proyectofinalaps;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NuevaTarea extends Fragment {

    // 1. Declaramos los objetos de la vista
    EditText etTitulo, etDescripcion, etFecha;
    Spinner spinnerMateria;
    RadioGroup rgPrioridad;
    Button btnGuardar;
    Base base;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el diseño que acabamos de crear
        return inflater.inflate(R.layout.fragment_nueva_tarea, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Enlazamos con los IDs del XML
        etTitulo = view.findViewById(R.id.etTitulo);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etFecha = view.findViewById(R.id.etFecha);
        spinnerMateria = view.findViewById(R.id.spinnerMateria);
        rgPrioridad = view.findViewById(R.id.rgPrioridad);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Inicializamos la Base de Datos
        base = new Base(getContext());

        // 3. Llenamos el Spinner (Combo) con materias de ejemplo
        String[] materias = {"Matemáticas", "Programación", "Inglés", "Historia", "Base de Datos", "Sistemas Distribuidos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, materias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateria.setAdapter(adapter);

        // 4. Programamos el Botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTarea();
            }
        });
    }

    private void guardarTarea() {
        // Validar que no estén vacíos
        if (etTitulo.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Escribe un título por favor", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la prioridad seleccionada
        String prioridad = "Media";
        int selectedId = rgPrioridad.getCheckedRadioButtonId();
        if (selectedId == R.id.rbAlta) prioridad = "Alta";
        else if (selectedId == R.id.rbBaja) prioridad = "Baja";

        // Crear objeto Tarea
        Tarea nuevaTarea = new Tarea(
                etTitulo.getText().toString(),
                etDescripcion.getText().toString(),
                spinnerMateria.getSelectedItem().toString(),
                prioridad,
                etFecha.getText().toString(),
                false // Completada = false al crearla
        );

        // Guardar en BD
        long id = base.agregarTarea(nuevaTarea);

        if (id > 0) {
            Toast.makeText(getContext(), "¡Tarea guardada con éxito!", Toast.LENGTH_SHORT).show();
            // Limpiar campos
            etTitulo.setText("");
            etDescripcion.setText("");
            etFecha.setText("");
        } else {
            Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}