//ui/main/MainActivity
package com.example.inmobiliaria.ui.main;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inmobiliaria.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiliaria.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  private AppBarConfiguration mAppBarConfiguration;
  private ActivityMainBinding binding;
  private FloatingActionButton fab;
  private MainViewModel vm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    vm= new ViewModelProvider(this).get(MainViewModel.class);

    setSupportActionBar(binding.appBarMain.toolbar);
    fab=findViewById(R.id.fab);
    fab.hide(); //Ocultamos

    binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show();
      }
    });
    DrawerLayout drawer = binding.drawerLayout;
    NavigationView navigationView = binding.navView;
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_inicio, R.id.nav_perfil, R.id.nav_inmueble, R.id.nav_inquilino, R.id.nav_contrato, R.id.nav_logout)
            .setOpenableLayout(drawer)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

    NavigationUI.setupWithNavController(navigationView, navController);

    navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
      if (destination.getId() == R.id.nav_inmueble) {
        fab.show();
        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.setOnClickListener(v ->
                navController.navigate(R.id.cargarInmuebleFragment)
        );
      } else {
        fab.hide();
      }
    });
    observarViewModel();
    vm.cargarDatos();

  }

  private void observarViewModel() {
    // Observa mensajes de error
    vm.getmMensaje().observe(this, mensaje -> {
      Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    });

    //

    //
    View headerView = binding.navView.getHeaderView(0);
    TextView tvNombre = headerView.findViewById(R.id.tVNombreHeader);
    TextView tvEmail = headerView.findViewById(R.id.tVcorreoHeader);
    //ImageView ivAvatar = headerView.findViewById(R.id.imgHeader);

    //
    vm.getmNombre().observe(this, nombre -> {
      tvNombre.setText(nombre);
    });

    //
    vm.getmCorreo().observe(this, email -> {
      tvEmail.setText(email);
    });
  }





  public FloatingActionButton getFab(){

    return fab;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
  }
}