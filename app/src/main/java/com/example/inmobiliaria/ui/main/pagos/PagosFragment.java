// ui/main/pagos/PagosFragment
package com.example.inmobiliaria.ui.main.pagos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inmobiliaria.databinding.FragmentPagosBinding;

import java.util.ArrayList;

public class PagosFragment extends Fragment {
  private PagosViewModel vm;
  private FragmentPagosBinding binding;
  private PagosAdapter adapter;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentPagosBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vm = new ViewModelProvider(this).get(PagosViewModel.class);

    configurarRecyclerView();
    configurarObservadores();

    vm.cargarPagos(getArguments());
  }

  private void configurarRecyclerView() {
    binding.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new PagosAdapter(new ArrayList<>());
    binding.rvPagos.setAdapter(adapter);
  }

  private void configurarObservadores() {
    // El ViewModel decide que mostrar
    vm.getMListaPagos().observe(getViewLifecycleOwner(), adapter::setListaPagos);
    vm.getMCargando().observe(getViewLifecycleOwner(), binding.progressBarPagos::setVisibility);
    vm.getMVisibilidadRecyclerView().observe(getViewLifecycleOwner(), binding.rvPagos::setVisibility);
    vm.getMCodigoContrato().observe(getViewLifecycleOwner(), binding.tvCodigoContrato::setText);
    vm.getMVisibilidadMensaje().observe(getViewLifecycleOwner(), binding.tvMensajeVacio::setVisibility);
    vm.getMensajeEnPantalla().observe(getViewLifecycleOwner(), binding.tvMensajeVacio::setText);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}