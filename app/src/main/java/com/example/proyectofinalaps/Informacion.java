package com.example.proyectofinalaps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Informacion extends Fragment {

    EditText etNombre, etCorreo;
    Button btnGuardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_informacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNombre = view.findViewById(R.id.etNombrePerfil);
        etCorreo = view.findViewById(R.id.etCorreoPerfil);
        btnGuardar = view.findViewById(R.id.btnGuardarPerfil);

        // 1. Cargar datos guardados previamente (si existen)
        SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String nombreGuardado = prefs.getString("nombre", "");
        String correoGuardado = prefs.getString("correo", "");

        etNombre.setText(nombreGuardado);
        etCorreo.setText(correoGuardado);

        // 2. Botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString();
            String nuevoCorreo = etCorreo.getText().toString();

            // Guardar en memoria del teléfono
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombre", nuevoNombre);
            editor.putString("correo", nuevoCorreo);
            editor.apply();

            // Actualizar el NavHeader (Llamando al método del Main)
            if (getActivity() instanceof MainActivity) {
                ((MainActivity)getActivity()).actualizarHeader(nuevoNombre, nuevoCorreo);
            }

            Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
        });
    }
}