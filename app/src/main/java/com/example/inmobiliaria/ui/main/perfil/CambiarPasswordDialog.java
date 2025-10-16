package com.example.inmobiliaria.ui.main.perfil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.DialogCambiarPasswordBinding;

public class CambiarPasswordDialog extends DialogFragment {
  private PerfilViewModel mv;
  private DialogCambiarPasswordBinding binding;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    mv = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);
    binding = DialogCambiarPasswordBinding.inflate(LayoutInflater.from(getContext()));

    // Observa los mensajes
    mv.getMmensaje().observe(this, msg ->
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
    );

    // Observa evento de cierre
    mv.getCerrarDialog().observe(this, cerrar -> {
      if (cerrar) dismiss();
    });

    // Usa binding directo
    return new AlertDialog.Builder(requireContext())
            .setTitle("Cambiar contraseña")
            .setView(binding.getRoot())
            .setPositiveButton("Confirmar", (dialog, which) -> {
              mv.onCambiarPasswordClick(
                      binding.etActual.getText().toString(),
                      binding.etNueva.getText().toString(),
                      binding.etConfirmar.getText().toString()
              );
            })
            .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
            .create();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null; // Limpieza
  }



}
