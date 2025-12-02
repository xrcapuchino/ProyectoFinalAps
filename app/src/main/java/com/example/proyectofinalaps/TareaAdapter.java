package com.example.proyectofinalaps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Usamos RecyclerView.ViewHolder genérico para soportar Tareas y Títulos
public class TareaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constantes para diferenciar los tipos de renglón
    private static final int TIPO_HEADER = 0; // Títulos (Ej: "Prioridad Alta")
    private static final int TIPO_TAREA = 1;  // Tareas normales

    private List<Object> listaItems; // Lista mixta

    public TareaAdapter(List<Object> listaItems) {
        this.listaItems = listaItems;
    }

    // Método clave: Decide si el renglón es un Título o una Tarea
    @Override
    public int getItemViewType(int position) {
        if (listaItems.get(position) instanceof String) {
            return TIPO_HEADER;
        } else {
            return TIPO_TAREA;
        }
    }

    // ---------------- CLASES VIEWHOLDER ----------------
    // 1. Para las Tareas (Tu diseño con imagen y botones)
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDetalles;
        CheckBox cbCompletada;
        ImageButton btnBorrar;
        ImageView ivMateria;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            cbCompletada = itemView.findViewById(R.id.cbCompletada);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            ivMateria = itemView.findViewById(R.id.ivMateria);
        }
    }

    // 2. Para los Títulos de Sección (Solo texto)
    public static class SeccionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeccion;
        public SeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de haber creado el layout 'item_seccion.xml' con un TextView id: tvSeccion
            tvSeccion = itemView.findViewById(R.id.tvSeccion);
        }
    }

    // ---------------- CREAR VISTAS ----------------
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TIPO_HEADER) {
            // Inflar diseño de título
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seccion, parent, false);
            return new SeccionViewHolder(view);
        } else {
            // Inflar diseño de tarea
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
            return new TareaViewHolder(view);
        }
    }

    // ---------------- ENLAZAR DATOS (BIND) ----------------
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Aplicar ANIMACIÓN a cualquier tipo de vista
        setAnimation(holder.itemView, position);

        if (getItemViewType(position) == TIPO_HEADER) {
            // --- LÓGICA DE TÍTULO ---
            String titulo = (String) listaItems.get(position);
            ((SeccionViewHolder) holder).tvSeccion.setText(titulo);

        } else {
            // --- LÓGICA DE TAREA ---
            Tarea tarea = (Tarea) listaItems.get(position);
            TareaViewHolder tareaHolder = (TareaViewHolder) holder;

            // 1. Textos
            tareaHolder.tvTitulo.setText(tarea.getTitulo());
            tareaHolder.tvDetalles.setText(tarea.getMateria() + " - " + tarea.getFecha());
            switch (tarea.getMateria()) {
                case "Programación":
                    tareaHolder.ivMateria.setImageResource(R.drawable.ic_progra); // Tus nombres de PNG
                    break;
                case "Matemáticas":
                    tareaHolder.ivMateria.setImageResource(R.drawable.ic_mates);
                    break;
                case "Inglés":
                    tareaHolder.ivMateria.setImageResource(R.drawable.ic_ingles);
                    break;
                case "Historia":
                    tareaHolder.ivMateria.setImageResource(R.drawable.ic_historia);
                    break;
                case "Sistemas Distribuidos":
                    tareaHolder.ivMateria.setImageResource(R.drawable.ic_sistm);
                    break;
                case "Base de Datos":
                    tareaHolder.ivMateria.setImageResource(R.drawable.ic_bd);
                    break;
                default:
                    tareaHolder.ivMateria.setImageResource(R.drawable.libro); // Default
                    break;
            }
            tareaHolder.cbCompletada.setOnCheckedChangeListener(null); // Limpiar listener previo
            tareaHolder.cbCompletada.setChecked(tarea.isCompletada());

            tareaHolder.cbCompletada.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tarea.setCompletada(isChecked);
                Base db = new Base(buttonView.getContext());
                db.actualizarTarea(tarea);
            });

            tareaHolder.btnBorrar.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Eliminar Tarea")
                        .setMessage("¿Seguro que quieres borrar '" + tarea.getTitulo() + "'?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            Base db = new Base(v.getContext());
                            db.eliminarTarea(tarea.getId());
                            listaItems.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toast.makeText(v.getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            tareaHolder.itemView.setOnClickListener(v -> {
                DetalleTarea fragmentDetalle = new DetalleTarea();
                Bundle args = new Bundle();

                args.putInt("id", tarea.getId());
                args.putString("titulo", tarea.getTitulo());
                args.putString("descripcion", tarea.getDescripcion());
                args.putString("materia", tarea.getMateria());
                args.putString("fecha", tarea.getFecha());
                args.putString("prioridad", tarea.getPrioridad());
                args.putBoolean("completada", tarea.isCompletada());

                fragmentDetalle.setArguments(args);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragmentDetalle)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        android.animation.ObjectAnimator animatorY = android.animation.ObjectAnimator.ofFloat(viewToAnimate, "translationY", 200f, 0f);
        animatorY.setDuration(500);
        android.animation.ObjectAnimator animatorAlpha = android.animation.ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0f, 1f);
        animatorAlpha.setDuration(500);
        android.animation.AnimatorSet animatorSet = new android.animation.AnimatorSet();
        animatorSet.playTogether(animatorY, animatorAlpha);
        animatorSet.start();
    }
}

