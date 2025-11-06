//ui/login/LoginActivity
package com.example.inmobiliaria.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;



import com.example.inmobiliaria.databinding.ActivityLoginBinding;
import com.example.inmobiliaria.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
  private LoginViewModel mv;
  private ActivityLoginBinding binding;
  private SensorManager sensorManager;
  private Sensor accelerometer;
  private LlamadaDetectar llamadaDetectar;
  private static final int REQUEST_CALL_PERMISSION = 1;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //inicializo el binding
    binding= ActivityLoginBinding.inflate(getLayoutInflater());

    EdgeToEdge.enable(this);
    setContentView(binding.getRoot());

    //iniciali el viewmodel
    mv= new ViewModelProvider(this).get(LoginViewModel.class);

    // NUEVO: Revisar si venimos de un Logout
    String logoutMsg = getIntent().getStringExtra("logout_message");
    if (logoutMsg != null && !logoutMsg.isEmpty()) {
      mv.setInitialMessage(logoutMsg);
      // Opcional: Limpiar el extra para que no se muestre si gira la pantalla
      getIntent().removeExtra("logout_message");
    }

    initLlamadaDetectar();
    observarViewModel();

    // Btn Login
    binding.btnLogin.setOnClickListener(v ->
            mv.login(
                    binding.eTLoginCorreo.getText().toString(),
                    binding.eTLoginContrasena.getText().toString()
            )
    );
  }

  private void initLlamadaDetectar() {
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    llamadaDetectar = new LlamadaDetectar();
    llamadaDetectar.setOnShakeListener(() -> {
      boolean tienePermiso = ContextCompat.checkSelfPermission(this,
              Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;

      mv.onTelefonoAgitado(tienePermiso);
    });
  }

  private void observarViewModel() {
    // Asigna el texto del mensaje
    mv.getmMensaje().observe(this, m ->
            binding.tVLoginMensaje.setText(m)
    );

    // Asigna la visibilidad del mensaje
    mv.getmMensajeVisibility().observe(this, visibility ->
            binding.tVLoginMensaje.setVisibility(visibility)
    );

    // Asigna la visibilidad del loader
    mv.getmCargarVisibility().observe(this, visibility ->
            binding.progressBar.setVisibility(visibility)
    );

    mv.getmIrAlmenu().observe(this, datos -> {
      Intent intent = new Intent(this, MainActivity.class);
      intent.putExtras(datos);
      startActivity(intent);
      finish();
    });

    mv.getSolicitarPermiso().observe(this, solicitar ->
            mostrarDialogoYSolicitarPermiso()
    );

    mv.getRealizarLlamada().observe(this, numeroTelefono ->
            realizarLlamada(numeroTelefono)
    );
  }

  private void mostrarDialogoYSolicitarPermiso() {
    new AlertDialog.Builder(this)
            .setTitle("Llamar a la Inmobiliaria")
            .setMessage("Desea llamar a la inmobiliaria?")
            .setPositiveButton("Llamar", (dialog, which) ->
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PERMISSION)
            )
            .setNegativeButton("Cancelar", null)
            .show();
  }

  private void realizarLlamada(String numeroTelefono) {
    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" + numeroTelefono));
    startActivity(callIntent);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    mv.onResultadoPermiso(requestCode, grantResults);

  }

  @Override
  protected void onResume() {
    super.onResume();
    if (accelerometer != null) {
      sensorManager.registerListener(llamadaDetectar, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(llamadaDetectar);
  }


}