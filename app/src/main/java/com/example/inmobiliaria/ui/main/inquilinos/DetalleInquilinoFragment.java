//ui/main/inquilinos/DetalleInquilinoFragment
package com.example.inmobiliaria.ui.main.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.data.model.Inquilino;
import com.example.inmobiliaria.databinding.FragmentDetalleInquilinoBinding;

public class DetalleInquilinoFragment extends Fragment {

  private DetalleInquilinoViewModel vm;
  private FragmentDetalleInquilinoBinding binding;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    vm = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);
    binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    // Observer para errores
    vm.getMError().observe(getViewLifecycleOwner(), error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    );

    // Observer para el inquilino
    vm.getMInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
      @Override
      public void onChanged(Inquilino inquilino) {
        //setear datos
        binding.tvCodigo.setText(String.valueOf(inquilino.getIdInquilino()));
        binding.tvNombre.setText(inquilino.getNombre());
        binding.tvApellido.setText(inquilino.getApellido());
        binding.tvDNI.setText(inquilino.getDni());
        binding.tvEmail.setText(inquilino.getEmail());
        binding.tvTelefono.setText(inquilino.getTelefono());
      }
    });

    // Cargar el inquilino con el Bundle recibido
    vm.cargarInquilino(getArguments());

    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }



}