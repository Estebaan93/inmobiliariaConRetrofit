//ui/main/contratos/ContratoFragment
package com.example.inmobiliaria.ui.main.contratos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentContratoBinding;

import java.util.ArrayList;

public class ContratoFragment extends Fragment {

  private ContratoViewModel vm;
  private FragmentContratoBinding binding;
  private ContratoAdapter adapter;

  public static ContratoFragment newInstance() {

    return new ContratoFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding= FragmentContratoBinding.inflate(inflater, container, false);

    vm= new ViewModelProvider(this).get(ContratoViewModel.class);

    configurarRecyclerView();
    observarViewModel();

    return binding.getRoot();
  }

  private void configurarRecyclerView() {
    GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
    binding.rvContratos.setLayoutManager(glm);

    adapter = new ContratoAdapter(
            getContext(),
            new ArrayList<>()
    );

    binding.rvContratos.setAdapter(adapter);
  }

  private void observarViewModel() {
    vm.getMCargando().observe(getViewLifecycleOwner(), cargando -> {
      binding.progressBarContrato.setVisibility(cargando ? View.VISIBLE : View.GONE);
      binding.rvContratos.setVisibility(cargando ? View.GONE : View.VISIBLE);
    });

    vm.getMError().observe(getViewLifecycleOwner(), error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    );

    vm.getMLista().observe(getViewLifecycleOwner(), inmuebles ->
            adapter.setLista(inmuebles)
    );
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }



}