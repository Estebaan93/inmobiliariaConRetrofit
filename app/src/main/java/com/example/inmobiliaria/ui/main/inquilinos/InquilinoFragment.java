//ui/main/inquilinos/InquilinoFragment
package com.example.inmobiliaria.ui.main.inquilinos;


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

import com.example.inmobiliaria.databinding.FragmentInquilinoBinding;

import java.util.ArrayList;


public class InquilinoFragment extends Fragment {

  private InquilinoViewModel vm;
  private FragmentInquilinoBinding binding;
  private InquilinoAdapter adapter;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentInquilinoBinding.inflate(inflater, container, false);
    vm = new ViewModelProvider(this).get(InquilinoViewModel.class);

    configurarRecyclerView();
    observarViewModel();

    return binding.getRoot();
  }

  private void configurarRecyclerView() {
    GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
    binding.itemListaInmuebles.setLayoutManager(glm);

    adapter = new InquilinoAdapter(
            getContext(),
            new ArrayList<>()
    );

    binding.itemListaInmuebles.setAdapter(adapter);
  }

  private void observarViewModel() {
    // Observer para el loader
    vm.getMCargando().observe(getViewLifecycleOwner(), cargando -> {
      binding.progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
      binding.itemListaInmuebles.setVisibility(cargando ? View.GONE : View.VISIBLE);
    });

    // Observer para errores
    vm.getMError().observe(getViewLifecycleOwner(), error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    );

    // Observer para la lista de inmuebles
    vm.getMLista().observe(getViewLifecycleOwner(), adapter::setLista);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}