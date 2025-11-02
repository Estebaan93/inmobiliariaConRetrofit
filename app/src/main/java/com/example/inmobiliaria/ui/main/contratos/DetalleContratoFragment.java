//ui/main/contratos/DetalleContratoFragment
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

    // Inicializar ViewModel
    vm = new ViewModelProvider(this).get(DetalleContratoViewModel.class);

    // Configurar observadores
    configurarObservadores();

    // Configurar listeners
    configurarListeners();

    // Cargar datos
    vm.cargarContrato(getArguments());
  }

  private void configurarObservadores() {
    // Observar datos del contrato
    vm.getMContratoId().observe(getViewLifecycleOwner(), contratoId ->
            binding.tvContratoId.setText(contratoId)
    );

    vm.getMFechaInicio().observe(getViewLifecycleOwner(), fechaInicio ->
            binding.tvFechaInicio.setText(fechaInicio)
    );

    vm.getMFechaFin().observe(getViewLifecycleOwner(), fechaFin ->
            binding.tvFechaFin.setText(fechaFin)
    );

    vm.getMMonto().observe(getViewLifecycleOwner(), monto ->
            binding.tvMonto.setText(monto)
    );

    vm.getMInquilinoNombre().observe(getViewLifecycleOwner(), inquilino ->
            binding.tvInquilinoNombre.setText(inquilino)
    );

    vm.getMInmuebleDireccion().observe(getViewLifecycleOwner(), direccion ->
            binding.tvInmuebleDireccion.setText(direccion)
    );

    // Observar estados
    vm.getMVisibilidadCargando().observe(getViewLifecycleOwner(), visibilidad -> {

    });

    vm.getMError().observe(getViewLifecycleOwner(), error -> {
      Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    });

    // Observar evento de navegacion
    vm.getMEventoNavegar().observe(getViewLifecycleOwner(), bundle -> {
      Navigation.findNavController(binding.getRoot())
              .navigate(R.id.action_detalleContratoFragment_to_pagosFragment, bundle);
      vm.finNavegacion();
    });
  }
  private void configurarListeners() {
    binding.btnVerPagos.setOnClickListener(v -> vm.navegarAPagos());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }




}
