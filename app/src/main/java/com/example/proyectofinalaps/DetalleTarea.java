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

public class DetalleTarea extends Fragment {

    EditText etTitulo, etDescripcion, etFecha;
    Spinner spinnerMateria;
    RadioGroup rgPrioridad;
    Button btnActualizar, btnRegresar;

    int idTarea = -1; // Para guardar el ID que recibimos
    boolean estadoActual = false; // Para no perder si estaba completada o no

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_tarea, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Enlazar Vistas
        etTitulo = view.findViewById(R.id.etDetalleTitulo);
        etDescripcion = view.findViewById(R.id.etDetalleDescripcion);
        etFecha = view.findViewById(R.id.etDetalleFecha);
        spinnerMateria = view.findViewById(R.id.spinnerDetalleMateria);
        rgPrioridad = view.findViewById(R.id.rgDetallePrioridad);
        btnActualizar = view.findViewById(R.id.btnActualizar);
        btnRegresar = view.findViewById(R.id.btnRegresar);

        // 2. Configurar Spinner (Mismo adaptador que en NuevaTarea)
        String[] materias = {"Matemáticas", "Programación", "Inglés", "Historia", "Base de Datos", "Sistemas Distribuidos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, materias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateria.setAdapter(adapter);

        // 3. RECIBIR DATOS Y RELLENAR CAMPOS
        Bundle args = getArguments();
        if (args != null) {
            idTarea = args.getInt("id"); // ¡IMPORTANTE!
            estadoActual = args.getBoolean("completada");

            etTitulo.setText(args.getString("titulo"));
            etDescripcion.setText(args.getString("descripcion"));
            etFecha.setText(args.getString("fecha"));

            // Seleccionar Materia correcta en el Spinner
            String materiaRecibida = args.getString("materia");
            if (materiaRecibida != null) {
                int position = adapter.getPosition(materiaRecibida);
                spinnerMateria.setSelection(position);
            }

            // Seleccionar Prioridad correcta
            String prioridadRecibida = args.getString("prioridad");
            if ("Alta".equals(prioridadRecibida)) {
                rgPrioridad.check(R.id.rbDetalleAlta);
            } else if ("Baja".equals(prioridadRecibida)) {
                rgPrioridad.check(R.id.rbDetalleBaja);
            } else {
                rgPrioridad.check(R.id.rbDetalleMedia);
            }
        }

        // 4. BOTÓN ACTUALIZAR
        btnActualizar.setOnClickListener(v -> {
            actualizarLaTarea();
        });

        // 5. BOTÓN REGRESAR
        btnRegresar.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
    }

    private void actualizarLaTarea() {
        // Validar
        if (etTitulo.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener prioridad seleccionada
        String nuevaPrioridad = "Media";
        int selectedId = rgPrioridad.getCheckedRadioButtonId();
        if (selectedId == R.id.rbDetalleAlta) nuevaPrioridad = "Alta";
        else if (selectedId == R.id.rbDetalleBaja) nuevaPrioridad = "Baja";

        // Crear objeto Tarea con los NUEVOS datos
        Tarea tareaActualizada = new Tarea(
                idTarea, // Usamos el MISMO ID (para que sepa cuál actualizar)
                etTitulo.getText().toString(),
                etDescripcion.getText().toString(),
                spinnerMateria.getSelectedItem().toString(),
                nuevaPrioridad,
                etFecha.getText().toString(),
                estadoActual // Mantenemos el estado de completada
        );

        // Llamar a la BD para actualizar
        Base db = new Base(getContext());
        int filasAfectadas = db.actualizarTarea(tareaActualizada);

        if (filasAfectadas > 0) {
            Toast.makeText(getContext(), "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack(); // Regresa al Home automáticamente
        } else {
            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }
}