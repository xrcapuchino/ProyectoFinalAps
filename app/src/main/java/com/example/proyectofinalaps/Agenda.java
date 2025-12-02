package com.example.proyectofinalaps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
public class Agenda extends Fragment {

    RecyclerView recyclerView;
    TextView tvSinPendientes;
    // Si tu clase de BD se llama 'Base', cambia AdminSQLiteOpenHelper por Base aquí
    Base dbHelper;
    TareaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_por_vencer);
        tvSinPendientes = view.findViewById(R.id.tvSinPendientes);

        // Inicializar BD
        dbHelper = new Base(getContext());

        cargarTareasPorVencer();
    }

    private void cargarTareasPorVencer() {
        List<Tarea> todas = dbHelper.obtenerTodasLasTareas();
        List<Tarea> pendientes = new ArrayList<>();

        // 1. FILTRAR: Solo queremos las que NO están completadas
        for (Tarea t : todas) {
            if (!t.isCompletada()) {
                pendientes.add(t);
            }
        }

        // 2. ORDENAR POR FECHA (Lógica inteligente)
        // Formato esperado: DD/MM/AAAA
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Collections.sort(pendientes, new Comparator<Tarea>() {
            @Override
            public int compare(Tarea t1, Tarea t2) {
                try {
                    Date fecha1 = sdf.parse(t1.getFecha());
                    Date fecha2 = sdf.parse(t2.getFecha());
                    return fecha1.compareTo(fecha2); // Devuelve negativo si f1 es antes que f2
                } catch (ParseException e) {
                    // Si la fecha está mal escrita, la mandamos al final
                    return 0;
                }
            }
        });

        // 3. MOSTRAR EN EL ADAPTADOR
        if (pendientes.isEmpty()) {
            tvSinPendientes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvSinPendientes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Convertimos a lista de Objetos para el adaptador
            List<Object> listaFinal = new ArrayList<>();

            // Agregamos un encabezado inicial
            listaFinal.add("Próximas Entregas 🔥");
            listaFinal.addAll(pendientes);

            // Reusamos tu TareaAdapter (¡Con imágenes, animaciones y todo!)
            adapter = new TareaAdapter(listaFinal);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    // Recargar si borras o editas algo y regresas
    @Override
    public void onResume() {
        super.onResume();
        if(dbHelper != null) cargarTareasPorVencer();
    }
}