package com.example.proyectofinalaps;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaramos las variables globales
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste para pantallas EdgeToEdge (barra transparente arriba)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Enlazamos los objetos con la vista (IDs deben coincidir con tu activity_main.xml)
        drawerLayout = findViewById(R.id.main); // Tu DrawerLayout principal
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.vista); // Tu NavigationView

        // 2. Configuramos el Toolbar (la barra azul de arriba)
        setSupportActionBar(toolbar);

        // 3. Configuramos el Menú Lateral (Drawer)
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close); // Asegúrate de tener estos strings o usa textos directos
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 4. Cargamos el fragmento por defecto al abrir la app (Home)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, new Home()) // 'frame' es el ID de tu FrameLayout contenedor
                    .commit();
            navigationView.setCheckedItem(R.id.home); // Marcar la opción en el menú
        }
    }

    // --- LÓGICA DE NAVEGACIÓN ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragmentSeleccionado = null;
        boolean fragmentTransaction = false;

        if (id == R.id.home) {
            fragmentSeleccionado = new Home();
            fragmentTransaction = true;
        }
        else if (id == R.id.nav_nueva_tarea) {
            fragmentSeleccionado = new NuevaTarea();
            fragmentTransaction = true;
        }
        else if (id == R.id.nav_agenda) {
            fragmentSeleccionado = new Agenda();
            fragmentTransaction = true;
        }
        else if (id == R.id.informacion) {
            fragmentSeleccionado = new Informacion();
            fragmentTransaction = true;
        }
        else if (id == R.id.cerrar_sesion) {
            // Cerrar la aplicación
            finishAffinity();
        }

        // Si seleccionamos una opción válida que carga fragmento, hacemos el cambio
        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragmentSeleccionado)
                    .commit();
        }

        // Cerramos el menú lateral después de elegir
        drawerLayout.closeDrawers();
        return true;
    }

    // Método para manejar el botón "Atrás" del celular y que cierre el menú si está abierto
    @Override
    public void onBackPressed() {
        // Usamos GravityCompat.START para referirnos al menú lateral izquierdo
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void actualizarHeader(String nombre, String correo) {
        View headerView = navigationView.getHeaderView(0);
        TextView tvNombre = headerView.findViewById(R.id.tvNombreHeader);
        TextView tvCorreo = headerView.findViewById(R.id.tvCorreoHeader);

        tvNombre.setText(nombre);
        tvCorreo.setText(correo);
    }

}