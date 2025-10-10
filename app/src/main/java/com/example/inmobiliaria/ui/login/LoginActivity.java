//ui/login/LoginActivity
package com.example.inmobiliaria.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.inmobiliaria.data.request.ApiClient;
import com.example.inmobiliaria.databinding.ActivityLoginBinding;
import com.example.inmobiliaria.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
  private LoginViewModel mv;
  private ActivityLoginBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //inicializo el binding
    binding= ActivityLoginBinding.inflate(getLayoutInflater());

    EdgeToEdge.enable(this);
    setContentView(binding.getRoot());

    //iniciali el viewmodel
    mv= new ViewModelProvider(this).get(LoginViewModel.class);

    mv.getmMensaje().observe(this, m->
            Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
            );



    mv.getmCargar().observe(this, visible ->
            binding.progressBar.setVisibility(visible ? View.VISIBLE : View.GONE)
    );

    mv.getmIrAlmenu().observe(this,datos->{
      Intent intent = new Intent(this, MainActivity.class);
      intent.putExtras(datos);
      startActivity(intent);
      finish();
    });

    // Btn Login
    binding.btnLogin.setOnClickListener(v ->
            mv.login(
                    binding.eTLoginCorreo.getText().toString(),
                    binding.eTLoginContrasena.getText().toString()
            )
    );


  }



}