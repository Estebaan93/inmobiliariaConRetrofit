//ui/main/logout/LogoutFragment
package com.example.inmobiliaria.ui.main.logout;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {

  private LogoutViewModel mv;

  public static LogoutFragment newInstance() {

    return new LogoutFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    mv= new ViewModelProvider(this).get(LogoutViewModel.class);

    //Observar evento del ViewModel
    mv.getVolverAlLogin().observe(getViewLifecycleOwner(), mensaje -> {
      // Volver al LoginActivity
      Intent intent = new Intent(requireActivity(), LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

      //nuevo05
      intent.putExtra("logout_message", mensaje);

      startActivity(intent);

      requireActivity().finish(); // Cierra el MainActivity actual
    });

    //Mostrat al abrir el fragment
    new AlertDialog.Builder(requireContext())
            .setTitle("Cerrar sesion")
            .setMessage("¿Desea salir de la aplicacion?")
            .setPositiveButton("Si, salir", (dialog, which) -> mv.cerrarSesion())
            .setNegativeButton("Cancelar", (dialog, which) -> {
              dialog.dismiss();
              requireActivity().onBackPressed();
            })
            .setCancelable(false)
            .show();

    return inflater.inflate(R.layout.fragment_logout, container, false);
  }



}