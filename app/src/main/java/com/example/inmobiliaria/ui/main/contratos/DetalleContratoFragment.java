// ui/main/contratos/DetalleContratoFragment
package com.example.inmobiliaria.ui.main.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentDetalleContratoBinding;

public class DetalleContratoFragment extends Fragment {
  private DetalleContratoViewModel vm;
  private FragmentDetalleContratoBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    vm = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
    configurarObservadores();
    vm.cargarContrato(getArguments());
  }

  private void configurarObservadores() {
    // Observadores para llenar la UI (sin cambios)
    vm.getMContratoId().observe(getViewLifecycleOwner(), binding.tvContratoId::setText);
    vm.getMFechaInicio().observe(getViewLifecycleOwner(), binding.tvFechaInicio::setText);
    vm.getMFechaFin().observe(getViewLifecycleOwner(), binding.tvFechaFin::setText);
    vm.getMMonto().observe(getViewLifecycleOwner(), binding.tvMonto::setText);
    vm.getMInquilinoNombre().observe(getViewLifecycleOwner(), binding.tvInquilinoNombre::setText);
    vm.getMInmuebleDireccion().observe(getViewLifecycleOwner(), binding.tvInmuebleDireccion::setText);

    vm.getMError().observe(getViewLifecycleOwner(), error ->
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show()
    );

    // VVV MODIFICADO: Observador de Contrato
    vm.getMContrato().observe(getViewLifecycleOwner(), contrato -> {
      binding.btnVerPagos.setOnClickListener(v -> {
        // El Fragment crea el Bundle y navega
        Bundle bundle = new Bundle();
        bundle.putInt("contratoId", contrato.getIdContrato());
        Navigation.findNavController(v)
                .navigate(R.id.action_detalleContratoFragment_to_pagosFragment, bundle);
      });
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}