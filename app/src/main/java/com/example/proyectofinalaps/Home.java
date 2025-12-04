package com.example.proyectofinalaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Home extends Fragment {

    RecyclerView recyclerView;
    TareaAdapter adapter;
    List<Tarea> listaTareas;
    Base dbHelper;
    LinearLayout tvSinTareas;
    Button btnClassroom; // 1. Declaramos el botón

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_home);
        tvSinTareas = view.findViewById(R.id.tvSinTareas);
        btnClassroom = view.findViewById(R.id.btnClassroom); // 2. Enlazamos

        // --- LÓGICA DEL BOTÓN CLASSROOM ---
        btnClassroom.setOnClickListener(v -> {
            String url = "https://classroom.google.com/"; // La página a la que iremos
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Verificamos si hay navegador seguro para abrirlo
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "No se encontró un navegador", Toast.LENGTH_SHORT).show();
            }
        });
        // -----------------------------------

        // Inicializar BD y cargar lista (Código que ya tenías)
        dbHelper = new Base(getContext());
        cargarLista();
    }
    private void cargarLista() {
        List<Tarea> todasLasTareas = dbHelper.obtenerTodasLasTareas();

        // Listas temporales
        List<Tarea> altas = new ArrayList<>();
        List<Tarea> medias = new ArrayList<>();
        List<Tarea> bajas = new ArrayList<>();
        List<Tarea> completadas = new ArrayList<>();

        // 1. Separar
        for (Tarea t : todasLasTareas) {
            if (t.isCompletada()) {
                completadas.add(t);
            } else if (t.getPrioridad().equals("Alta")) {
                altas.add(t);
            } else if (t.getPrioridad().equals("Media")) {
                medias.add(t);
            } else {
                bajas.add(t);
            }
        }

        // 2. Ordenar por fecha (Método burbuja simple o Collections.sort)
        // Nota: Esto requiere que tu fecha sea AAAA/MM/DD para ordenar texto directo,
        // o usar un Comparador de fechas real. Por ahora ordenaremos texto simple.
        ordenarPorFecha(altas);
        ordenarPorFecha(medias);
        ordenarPorFecha(bajas);
        ordenarPorFecha(completadas);

        // 3. Construir la lista final Mezclada (Strings y Tareas)
        List<Object> listaFinal = new ArrayList<>();

        if (!altas.isEmpty()) {
            listaFinal.add("Urgente"); // Header
            listaFinal.addAll(altas);
        }
        if (!medias.isEmpty()) {
            listaFinal.add("Por hacer ️"); // Header
            listaFinal.addAll(medias);
        }
        if (!bajas.isEmpty()) {
            listaFinal.add("Sin prisa"); // Header
            listaFinal.addAll(bajas);
        }
        if (!completadas.isEmpty()) {
            listaFinal.add("Completadas"); // Header
            listaFinal.addAll(completadas);
        }

        // 4. Mandar al adaptador
        if (listaFinal.isEmpty()) {
            tvSinTareas.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvSinTareas.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new TareaAdapter(listaFinal);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    // Pequeño helper para ordenar strings de fecha
    private void ordenarPorFecha(List<Tarea> lista) {
        Collections.sort(lista, new Comparator<Tarea>() {
            @Override
            public int compare(Tarea t1, Tarea t2) {
                return t1.getFecha().compareTo(t2.getFecha());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dbHelper != null) {
            cargarLista();
        }
    }
}